```java 
import java.util.Scanner;

public class Palindrome {

	public static void main(String[] args) {

		// create final int MAX_CHARS = 80; restricts character count
		final int MAX_CHARS = 80;
		// create char response; for do while loop to rerun program at users request.
		char retest = 'n';
		// import scanner for userinput and define but dont initialize yet
		Scanner scanner;
		// start the do loop and then initialize the scanner
		do {

			scanner = new Scanner(System.in);
			// prompt user and get the phrase from the user
			System.out.println("Enter a string to test if it is a Palindrome: ");

			// create a string variable to hold the input, use nextLine() to ignore spaces
			String phrase = scanner.nextLine();
			// make sure input doesnt violate MAX_CHARS if(phrase.length() > MAX_CHARS) warn
			// user, repeat question
			if (phrase.length() > MAX_CHARS) {
				System.out.println("Please enter less than 80 characters");
				phrase = scanner.nextLine();
				// else input was valid
			} else {

				if (isPalindrome(phrase)) {
					System.out.println("The phrase is a palindrome");
				} else {
					System.out.println("The phrase is not a palindrome");
				}

				//prompt user to see if they would like to run the program again.
				System.out.print("\nContinue testing Palindromes?");
				retest = scanner.next().toLowerCase().charAt(0); 

			}
		}


		// end do while loop.
		while (retest == 'y');

		scanner.close();
	}

	// Palindrome function which returns boolean if phrase is a Palindrome
	public static boolean isPalindrome(String phrase) {
		// Remove spaces and convert letters to lower case
		phrase = phrase.replaceAll(" ", "").toLowerCase(); // searches for first arg, replaces w/ second

		// loop through phrase to see if its a Palindrome

		// dividing by 2 checks the end characters and ignores the middle individual
		// characters of the string array
		for (int i = 0; i < phrase.length() / 2; i++) {

			if (phrase.charAt(i) != phrase.charAt(phrase.length() - 1 - i)) {
				return false;
			}
			// if false we dont have a palindrome, if it exits w/o error, return true.
		}
		return true;
	}

}
```
