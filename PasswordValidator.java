import java.util.Scanner;

public class PasswordValidationLab {

	public static void main(String[] args) {
		// read in user input into string
		String password;
		Scanner inputScanner = new Scanner(System.in);

		System.out.println("Enter a password that meets the following criteria:");
		System.out.print("Must have 8 characters.\nCharacters must be numbers or letters only.\n");
		System.out.print("You must use atleast 2 numbers in your password: ");
		do {
			// check if input is valid
			password = inputScanner.next();
			// if valid, print valid password

			if (isValid(password)) {
				System.out.println("Password is valid!");

				// else print invalid
			} else {
				System.out.println("Password is not valid");
				System.out.print("Please enter a Valid password...");

			}

		} while (!isValid(password));
		
		inputScanner.close();

	} // end of main

	public static boolean isValid(String password) {
		// isValid - loop through string characters,
		for (int index = 0; index < password.length(); index++) {
			// make sure alpha numeric
			if (!Character.isLetterOrDigit(password.charAt(index))) {
				System.out.println("Password can only contain letters and digits: ");
				return false;
			}
		}
		// if length < 8 false,
		if (password.length() < 8) {
			System.out.println("Password must contain atleast 8 characters: ");
			return false;
		}

		// count digits to make sure >= 2
		int digitCount = 0;
		for (int index = 0; index < password.length(); index++) {
			if (Character.isDigit(password.charAt(index))) {
				digitCount++;
			}
		}
		if (digitCount < 2) {
			System.out.println("Password must have atleast 2 numbers: ");
			return false;

		}

		return true;
	}

}
