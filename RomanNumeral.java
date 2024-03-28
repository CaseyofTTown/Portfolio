
public class RomanNumeral {

	//a java program that takes a number entered in roman numerals and convert it to decimal

	//Roman has the following requirements
	//store a number as a roman numeral
	//-variables, string to store roman numeral string, int for its value
	private String romanNumeral; //hold string of romanNumeral
	private int decimalNumber;

	//convert and store a number into a decimal
	//constructor given a roman numeral string
	RomanNumeral(String romanNumeral){
		setRoman(romanNumeral);
	}

	//print the number as a roman numeral or decimal as requested
	public void printDecimal() {
		System.out.println(decimalNumber);
	}
	public void printRomanNumeral() {
		System.out.println(romanNumeral);
	}
	
	//can be used reset values given new number
	public void setRoman(String romanNumeral) {
		this.romanNumeral = romanNumeral;
		romanToDecimal();
	}
	
	//romanToDecimal
	public void romanToDecimal() {
		int sum = 0; //track of total
		int previousVal = 1000; //highest value we're expecting
		
		//iterate over each character in the string, add val to sum, 
		for(int i = 0; i < romanNumeral.length(); i++) {
			switch(romanNumeral.charAt(i)) {
			case 'M':
				sum += 1000;
				if(previousVal < 1000) {
					sum = sum - 2 * previousVal;
				}
				previousVal = 1000;
				break;
				
			case 'D':
				sum += 500;
				if(previousVal < 500) {
					sum = sum -2 * previousVal;
				}
				previousVal = 500;
				break;
				
			case 'C':
				sum += 100;
				if(previousVal < 100) {
					sum = sum -2 * previousVal;
				}
				previousVal = 100;
				break;
				
			case 'L':
				sum += 50;
				if(previousVal < 50) {
					sum = sum -2 * previousVal;
				}
				previousVal = 50;
				break;
				
			case 'X':
				sum += 10;
				if(previousVal < 10) {
					sum = sum -2 * previousVal;
				}
				previousVal = 10;
				break;
			case 'V':
				sum += 5;
				if(previousVal < 5) {
					sum = sum -2 * previousVal;
				}
				previousVal = 5;
				break;
			case 'I':
				sum += 1; 
				previousVal = 1; //lowest possible value
			}
			
		}
		//set the decimalNumber to the sum
		decimalNumber = sum;
	}
	
	
	//M = 1000, D = 500, C = 100, L = 50, X = 10, V = 5, I = 1,

	//test using MCXIV, CCCLIX, MDCLXVI

  //The main class to test and show usage of this class :)
}



import java.util.Scanner;

public class TestRomanNumeral {


	public static void main(String[] args) {
		//test using MCXIV, CCCLIX, MDCLXVI
		
		/**
		 * input takes user input
		 * repeat used for the loop to run program again 
		 */
		Scanner input;
		String repeat = "X";
		//do while loop to offer user multiple tests
		do {
			input = new Scanner(System.in);
			//prompt user to enter roman numeral
			System.out.print("Enter a roman numeral: ");
			String romanString = input.next().toUpperCase(); //catch roman Numeral string 
		
			RomanNumeral roman = new RomanNumeral(romanString);
			//print results and prompt user if theyd like to repeat 
			System.out.print("The equivalent of the roman numeral " + romanString
					+ " is ");
			roman.printDecimal();
			
			
			
			
			//ask user if theyd like to run again
			System.out.println("Would you like to run the program again? ");
			repeat = input.next().toUpperCase();
			
			
		} while(repeat.contains("Y"));
		
		input.close();
		System.out.println("Program terminated successfully without error :)");
		
	

	}

}

