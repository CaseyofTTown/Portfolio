```java
import java.util.Scanner;

public class labOne {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		double [] myList = new double [5];
		int indexOfMaxNumber = 0;
		double temp;

		System.out.print("Enter " + myList.length + " values: ");
		for (int i = 0; i < myList.length; i++) {
			while (!input.hasNextDouble()) {
				System.out.println("That's not a number! Try again.");
				input.next();
			}
			myList[i] = input.nextDouble();
		}

		double max = myList[0];
		printArray(myList);

		for (int x = 1; x < myList.length; x++) {
			if(myList[x] > max) {
				max = myList[x];
				indexOfMaxNumber = x;
			}
		}

		System.out.printf("\nThe highest value in the array is: %.3f \n" , max);
		System.out.println("Which is at index: " + indexOfMaxNumber);

		System.out.println("Moving all the values to the left by 1 place now...");

		double tempForMoving = myList[0];
		for (int a = 1; a < myList.length; a++) {
			myList[a - 1] = myList[a];
		}
		myList[myList.length - 1] = tempForMoving;

		printArray(myList);

		System.out.println("Shuffling the values randomly now...");

		for (int l = 0; l < myList.length; l++) {
			int random = (int)(Math.random() * myList.length);
			temp = myList[l];
			myList[l] = myList[random];
			myList[random] = temp;
		}

		printArray(myList);

		System.out.println("And now we reverse the last list generated...");
		double[] reversedList = reverseArray(myList);
		for (double value: reversedList) {
			int counter = 1;
			System.out.printf("The values of myList at index %d: %.3f \n",counter, value);
			counter++;
		}
	}

	public static void printArray(double[] array) {
		for(int index = 0; index < array.length; index++) {
			System.out.printf("The value of myList[%d] is %.3f\n", index, array[index]);
		}
	}

	public static double[] reverseArray(double [] myList) {
		double [] result = new double [myList.length];

		for (int i = 0, j = result.length - 1; i < myList.length; i++ ,j--) {
			result[i] = myList[j];
		}

		return result;
	}
}

  ```
