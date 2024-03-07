```kotlin
// This function is called when the register button is clicked
    suspend fun onRegisterClicked() {
        // Check if the email and password are valid
        if (registrationState.isEmailValid.value && registrationState.isPasswordValid.value) {
            // Attempt to sign up with the provided email and password
            signUpWithEmailandPassword(
                registrationState.email.value,
                registrationState.password.value
            ) {
                // If the email is already in use, call the onEmailAlreadyInUse function
                onEmailAlreadyInUse()
            }
        } else {
            _uiState.value = UiState.Error("problem in onRegisterClicked")
        }
    }

    //called when registering
    private fun onEmailAlreadyInUse() {
        _uiState.value = UiState.SignedOut
        clearNavigationEvents()
        navigate(Destinations.Pantry.route)
    }

    suspend fun signInWithEmailnPassword(email: String, password: String) {
        Log.d(TAG, "sign In w/ email n password called")
        _uiState.value = UiState.InProgress
        Log.d(TAG, "UiState.InProgress")
        try {
            _uiState.value = authenticateUser(email, password, repository::signIn)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d(TAG, "Error in SignInw/emailnPassword \n$e")
            _uiState.value = UiState.Error(e.message)
        } finally {
            if (loginData.rememberMe) {
                Log.d(TAG, "rememberPassword was true, checking prefs and saving")
                repository.saveEmailtoPrefs(loginData.email)
                repository.savePasswordToPrefs(loginData.password)
            }
        }
    }

    private suspend fun signUpWithEmailandPassword(
        email: String, password: String,
        onEmailAlreadyInUse: suspend () -> Unit
    ) {
        Log.d(TAG, "signUpw/email/pass called")
        _uiState.value = UiState.InProgress
        try {
            _uiState.value = authenticateUser(email, password) { e, p ->
                //authFunction below
                repository.signUp(e, p, onEmailAlreadyInUse)
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d(TAG, "Error in signUpWithEmailandPassword \n$e")
            _uiState.value = UiState.Error(e.message)
            Log.d(TAG, "UiState changed to Error + $e")
        }
    }


    private suspend fun authenticateUser(
        email: String,
        password: String,
        authFunction: suspend (String, String) -> UiState
    ): UiState {
        var result: UiState = UiState.InProgress
        viewModelScope.launch {
            Log.d(TAG, "calling authentication function")
            result = withContext(Dispatchers.IO) {
                try {
                    authFunction(email, password)
                } catch (e: FirebaseAuthUserCollisionException) {
                    //case where email is already in use
                    _uiState.value = UiState.Error("Email is Already in use, attempting login")
                    onEmailAlreadyInUse()
                    try {
                        repository.signIn(email, password)
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        //the case where password doesnt match existing email
                        _showLoginDialog.value = true
                        UiState.Error("Incorrect password for existing account")
                    }
                }
            }
            when (result) {
                is UiState.SignedIn -> {
                    //trigger navigation
                    Log.d(
                        TAG, "UiState.SignedIn\n"
                    )
                    _showLoginDialog.value = false
                    _isUserSignedIn.value = true
                    //greet user
                    if (currentAppUser.value?.userName != "Default") {
                        showSnackbar("Welcome ${currentAppUser.value?.userName}")
                    } else {
                        showSnackbar("Welcome ${currentAppUser.value?.email}")
                    }
                    if (userManager.profilePictureBitmap.value == null
                        && currentAppUser.value?.profilePictureUrl != null
                    ) {
                        viewModelScope.launch(Dispatchers.IO) {
                            Log.d(TAG, "getting picture from url")
                            val picUrl = currentAppUser.value!!.profilePictureUrl
                            if (picUrl != null) {
                                val bitmap = repository.urlToBitmap(picUrl)
                                withContext(Dispatchers.Main) {
                                    Log.d(TAG, "updating profilePictureBitmap")
                                    if (bitmap != null) {
                                        userManager.getProfilePicfromViewModel(bitmap)
                                    }
                                }
                            }
                        }
                    }
                    //update user manager class's values with ones from database
                    try {
                        userManager.updateUserManager(currentAppUser.value)

                    } catch (e: FirebaseAuthException) {
                        Log.e(TAG, "unable to update UserManager", e)
                    }
                    _uiState.value = result
                    Log.d(TAG, _uiState.value.toString())
                }

                is UiState.Error -> {
                    Log.d(TAG, "set to Error")
                    _uiState.value = result
                }

                is UiState.InProgress -> {
                    Log.d(TAG, "set to InProgress")
                    _uiState.value = result
                }

                is UiState.SignedOut -> {
                    Log.d(TAG, "set to SignedOut")
                    _uiState.value = result
                }

                else -> {
                    Log.d(TAG, "No case met for UiState...")
                }
            }
        }
        return result
    }


    //this is the UiState class which is used extensively throughout the project for managing State and triggering ui events 

    //state object used to trigger composables from ViewModel
//was first implemented for navigation purposes after OneTap()
sealed class UiState {
    object SignedOut: UiState()
    object InProgress : UiState()
    data class Error (val message: String?): UiState()
    object SignedIn : UiState()
    data class Success(val message: String?, val actionType: ActionType) : UiState()
    object IsEditing : UiState()
    object Neutral : UiState()
    object IsViewingAnotherUsersProfile : UiState() // for user profile screen ui settings
}

//here is some of the repository code which allows creating users / signing in / saving password to encrypted prefs which i now know is not best practice

 //called when registering for the first time, creates document to store AppUser in firestore
    suspend fun signUp(
        email: String,
        password: String,
        onEmailAlreadyInUse: suspend () -> Unit
    ): UiState = coroutineScope {
        return@coroutineScope suspendCancellableCoroutine<UiState> { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "firebaseAuth Successful, signing in with $email")
                        val firebaseUser = task.result?.user
                        if (firebaseUser != null) {
                            Log.d(TAG, "user != null == ${firebaseUser.email}")
                            // Initialize the user object with default data
                            val defaultUser = AppUser(
                                uid = firebaseUser.uid,
                                userName = "Default",
                                email = firebaseUser.email ?: "",
                                areaFrom = "Default",
                                savedRecipes = listOf(),
                                ratedRecipes = mapOf(),
                                profilePictureUrl = "Default",
                                bio = "Default",
                                followers = listOf(),
                                following = listOf(),
                                foodAllergy = listOf(),
                                experiencePoints = 0,
                                isPremium = false,
                                subscriptionEndDate = null,
                                hasUpdatedProfile = false
                            )
                            // Send the default user data to Firebase
                            launch {
                                createUser(defaultUser)
                            }
                            _currentAppUser.value = defaultUser
                        } else {
                            Log.d(TAG, "Error! user == null")
                        }
                        continuation.resume(UiState.SignedIn)
                    } else {
                        //if another account exists with this email...
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            launch {
                                //shows snackbar, triggers progress spinner, navigates to pantry
                                onEmailAlreadyInUse()
                                try {
                                    val signInResult = signIn(email, password)
                                    if (continuation.isActive) {
                                        continuation.resume(signInResult)
                                    }
                                } catch (e: FirebaseAuthInvalidCredentialsException) {
                                    if (continuation.isActive) {
                                        continuation.resume(UiState.Error("Incorrect password for existing account."))
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "Error signing up user w/ email/pass \n," + task.exception)
                            val uiStateError =
                                FirebaseAuthExceptionHandler().handle(task.exception as FirebaseAuthException)
                            if (continuation.isActive) {
                                continuation.resume(uiStateError)
                            }
                        }
                    }
                }
        }
    }


    //sign in with email and password to firestore
    suspend fun signIn(email: String, password: String): UiState {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Signing in with user email and password")
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result?.user
                if (firebaseUser != null) {
                    val appUser = firebaseUser.toUser()
                    _currentAppUser.value = appUser
                    UiState.SignedIn
                } else {
                    Log.d(TAG, "User == null, unable to update state")
                    UiState.Error("User does not exist.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in signIn \n", e)
                _currentAppUser.value = null
                if (e is FirebaseAuthException) {
                    exceptionHandler.handle(e)
                } else {
                    UiState.Error("Unknown error occurred.")
                }
            }
        }
    }


    //called to create the user document in firestore upon successful registration
    private suspend fun createUser(user: AppUser): Result<Boolean> {
        val userMap = hashMapOf(
            "uid" to user.uid,
            "username" to user.userName,
            "email" to user.email,
            "areaFrom" to user.areaFrom,
            "savedRecipes" to user.savedRecipes,
            "ratedRecipes" to user.ratedRecipes,
            "profilePictureUrl" to user.profilePictureUrl,
            "bio" to user.bio,
            "followers" to user.followers,
            "following" to user.following,
            "allergies" to user.foodAllergy,
            "experiencePoints" to user.experiencePoints,
            "hasUpdatedProfile" to user.hasUpdatedProfile
        )

        return try {
            Log.d(TAG, "Adding new user to database")
            db.collection("Users").document(user.uid).set(userMap).await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating user", e)
            Result.failure(e)
        }
    }

    //accepts AppUser and uploads to Firestore "Users" collection
    //called by syncAppUserWithFirebaseUser
    private fun updateUserInDatabase(user: AppUser) {
        val userMap = hashMapOf(
            "uid" to user.uid,
            "username" to user.userName,
            "email" to user.email,
            "areaFrom" to user.areaFrom,
            "savedRecipes" to user.savedRecipes,
            "ratedRecipes" to user.ratedRecipes,
            "profilePictureUrl" to user.profilePictureUrl,
            "bio" to user.bio,
            "followers" to user.followers,
            "following" to user.following,
            "allergies" to user.foodAllergy,
            "experiencePoints" to user.experiencePoints,
            "isPremium" to user.isPremium,
            "subscriptionEndDate" to user.subscriptionEndDate,
            "hasUpdatedProfile" to user.hasUpdatedProfile
        )
        db.collection("Users").document(user.uid).update(userMap)
    }

    //public fun exposed to viewmodel to update user profile
    //also updates _currentAppUser
    fun uploadCurrentAppUserToFirestore(updatedUser: AppUser) {
        updateUserInDatabase(updatedUser)
        _currentAppUser.value = updatedUser
    }

    fun saveEmailtoPrefs(email: String) {
        val emailKey = "email"
        if (!sharedPreferences.contains(emailKey)) {
            Log.d(TAG, "Shared preferences does not have an email, adding: $email")
            sharedPreferences.edit().putString(emailKey, email).apply()
        } else {
            Log.d(TAG, "Shared preferences already contains an email")
        }
    }

    fun savePasswordToPrefs(password: String) {
        val passwordKey = "password"
        if (!sharedPreferences.contains(passwordKey)) {
            Log.d(TAG, "Shared preferences does not have a password saved, adding now")
            sharedPreferences.edit().putString(passwordKey, password).apply()
        } else {
            Log.d(TAG, "Shared preferences already has a password saved...")
        }
    }

    fun signOut() {
        Log.d(TAG, "Signing out from Firestore")
        firebaseAuth.signOut()
        UiState.SignedOut
        _currentAppUser.value = null
    }

    /*
    a default user is sent to firebase in SignUp and here
    the AppUser is populated with those values
     */
    private suspend fun FirebaseUser.toUser(): AppUser {
        // Fetch the user document from Firestore
        val userDocument = db.collection("Users").document(uid).get().await()
        Log.d(TAG, "Populating apps user values with firestores user values")

        // Map the document fields to AppUser properties
        return AppUser(
            uid = uid,
            userName = userDocument["username"] as? String ?: "",
            email = userDocument["email"] as? String ?: "",
            areaFrom = userDocument["areaFrom"] as? String ?: "",
            savedRecipes = userDocument["savedRecipes"] as? List<String> ?: listOf(),
            ratedRecipes = userDocument["ratedRecipes"] as? Map<String, Int> ?: mapOf(),
            profilePictureUrl = userDocument["profilePictureUrl"] as? String,
            bio = userDocument["bio"] as? String,
            followers = userDocument["followers"] as? List<String> ?: listOf(),
            following = userDocument["following"] as? List<String> ?: listOf(),
            foodAllergy = userDocument["allergies"] as? List<String> ?: listOf(),
            experiencePoints = (userDocument["experiencePoints"] as? Number)?.toInt() ?: 0,
            isPremium = userDocument["isPremium"] as? Boolean ?: false,
            subscriptionEndDate = userDocument["subscriptionEndDate"] as? Date,
            hasUpdatedProfile = userDocument["hasUpdatedProfile"] as? Boolean ?: false
        )
    }

    //gets profiles by their UID for the followers/following ui
    suspend fun fetchUserProfiles(uids: List<String>): List<AppUser> {
        val users = mutableListOf<AppUser>()

        for (uid in uids) {
            val user = uid.toAppUser()
            if (user != null) {
                users.add(user)
            }
        }
        return users
    }

    private suspend fun String.toAppUser(): AppUser? {
        val userDocument = db.collection("Users")
            .document(this).get().await()

        return if (userDocument.exists()) {
            AppUser(
                uid = this,
                userName = userDocument["username"] as? String ?: "",
                email = userDocument["email"] as? String ?: "",
                areaFrom = userDocument["areaFrom"] as? String ?: "",
                savedRecipes = userDocument["savedRecipes"] as? List<String> ?: listOf(),
                ratedRecipes = userDocument["ratedRecipes"] as? Map<String, Int> ?: mapOf(),
                profilePictureUrl = userDocument["profilePictureUrl"] as? String,
                bio = userDocument["bio"] as? String,
                followers = userDocument["followers"] as? List<String> ?: listOf(),
                following = userDocument["following"] as? List<String> ?: listOf(),
                foodAllergy = userDocument["allergies"] as? List<String> ?: listOf(),
                experiencePoints = (userDocument["experiencePoints"] as? Number)?.toInt() ?: 0,
                isPremium = userDocument["isPremium"] as? Boolean ?: false,
                subscriptionEndDate = userDocument["subscriptionEndDate"] as? Date,
                hasUpdatedProfile = userDocument["hasUpdatedProfile"] as? Boolean ?: false
            )
        } else {
            null
        }
    }


    fun isUserAuthenticated(): Boolean {
        return try {
            firebaseAuth.currentUser != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user authentication", e)
            false
        }
    }

    private suspend fun getUser(): AppUser? {
        Log.d(
            TAG, "Getting user from firebase \n" +
                    "User email: ${firebaseAuth.currentUser?.email}"
        )
        return firebaseAuth.currentUser?.toUser()
    }

    //could be used to update the basic info firebase Auth can store
    //currently the app stores all of this in firestore database so...
    fun updateUserProfile(displayName: String?, photoUrl: Uri?) {
        val user = firebaseAuth.currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .setPhotoUri(photoUrl)
            .build()
        user?.updateProfile(profileUpdates)
    }

    //TODO need to include a textbutton to call this from VM
    fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
        Log.d(TAG, "Sending password reset email to $email")
    }

    //TODO need to setup a button for this to be called
    fun deleteUser() {
        val user = firebaseAuth.currentUser
        Log.d(TAG, "Deleting user $user")
        user?.delete()
    }

    //TODO need to setup this in settings menu
    fun sendEmailVerification() {
        val user = firebaseAuth.currentUser
        user?.sendEmailVerification()
    }

    //TODO need to setup some logic to show if user is verified w/ this
    fun isEmailVerified(): Boolean {
        val user = firebaseAuth.currentUser
        return user?.isEmailVerified ?: false
    }

    suspend fun urlToBitmap(url: String): ImageBitmap {
        val client = HttpClient(Android)
        val byteArray = client.get<ByteArray>(url)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return bitmap.asImageBitmap()
    }

    //the user class

    import java.util.Date


data class AppUser(
    val uid: String,
    val userName: String,
    val email: String,
    val areaFrom: String = "",
    val savedRecipes: List<String> = listOf(),
    val ratedRecipes: Map<String, Int> = mapOf(),
    val profilePictureUrl: String? = null,
    val bio: String? = "",
    val followers: List<String> = listOf(),
    val following: List<String> = listOf(),
    val foodAllergy: List<String> = listOf(),
    val experiencePoints: Int = 0,
    val level: Int = experiencePoints/100,
    val isPremium: Boolean = false,
    val subscriptionEndDate: Date? = null, 
    val hasUpdatedProfile: Boolean = false,
)

//simple class for pulling email and password from shared prefs 

class LoginData(private val repository: RecipeRepository) {
    var email by mutableStateOf(repository.sharedPreferences.getString("email", "") ?: "")
    var password by mutableStateOf(repository.sharedPreferences.getString("password", "") ?: "")
    var rememberMe by mutableStateOf(true)
}

//


```
