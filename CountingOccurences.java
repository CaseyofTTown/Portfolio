
import java.util.Scanner;

public class CountingOccurances {

	public static void main(String[] args) {
		//1. Declare variables, input scanner, array w/ counts, int inputNumber
		Scanner userInput = new Scanner(System.in);
		int [] counter = new int [100]; //can input up to 100 values
		int inputNumber;
		//2. Prompt user to give a list of numbers between 1 and 100, ending in 0
		System.out.print("Enter integers between 1 and 100, ending in 0: ");
		//read first input before loop to make sure while loop functions
		inputNumber = userInput.nextInt();
		
		//3. Create a loop, read in each input, check if its within range and add to counter
		//terminates when number is 0
		while (inputNumber != 0) {
			if(inputNumber <= 100 && inputNumber >=  1)
				counter[inputNumber -1] ++;
			inputNumber = userInput.nextInt();
		}
		
		//4. Display loop, from 0-99. If count at that index > 0, print how many times it occurs
		for (int i = 0; i < 100; i++) {
			if(counter[i] > 0)
				//if counter == 1 itll say time, if count > 1 itll outputs times
				System.out.printf("%d occurs %d" + ((counter[i]==1) ? " time\n" : " times\n")  , i+1, counter[i] );
		}



	}

}
