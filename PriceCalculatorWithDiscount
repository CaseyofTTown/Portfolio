public class PriceCalculator {

	public static void main(String[] args) {
		
		//declare variables, numProductsToBuy, priceOfProduct, orderTotalPrice, 
		//orderDiscountedPriceTotal
		int numProductsToBuy, iterator;
		double priceOfProduct = 0.0;
		double tempToCheckValidity = 0.0;
		double orderTotalPrice = 0.0, orderDiscountedPriceTotal = 0.0;
		
		Scanner userInput = new Scanner(System.in);

		//Prompt user to enter the number of products they wish to buy
		System.out.println("Enter the amount of products you plan to buy: ");
		numProductsToBuy = userInput.nextInt(); 
		
		
		for(iterator = 1; iterator <= numProductsToBuy; iterator++) {
			//Prompt user to enter a price for each product
			System.out.print("Please Enter the cost of item number " + iterator + ":");
			tempToCheckValidity = userInput.nextDouble();
			while (tempToCheckValidity <= 0.00) {
				System.out.print("\n I'm sorry, you must enter a number above 0. ");
				System.out.print("Please Enter the cost of item number " + iterator + ":");
				tempToCheckValidity = userInput.nextDouble();
				if (tempToCheckValidity > 0) {
					break;
				}
			}
			//breaks out of or avoids while loop here so add valid input to total. 
			priceOfProduct += tempToCheckValidity;
		}
		
		//if the user buys more than 5 products, apply 10% discount to total price
		if (numProductsToBuy >= 5) {
			System.out.print("You purchased 5 of more products so you get a 10% discount! :) \n");
			System.out.printf("The Total cost for " + numProductsToBuy + " items before discount is: $%.2f\n", priceOfProduct);
			orderDiscountedPriceTotal = (priceOfProduct * .9); 
			System.out.printf("The total cost for " + numProductsToBuy + " with your discount is $%.3f", orderDiscountedPriceTotal);
			
		}
		else {
			//output total and discount if applicable
			System.out.print("You must purchase 5+ items to receive a discount so you get the standard price.\n");
			System.out.printf("The Total cost for " + numProductsToBuy + " items is: $%.2f", priceOfProduct);
			
			
		}
	

		

	}

}
