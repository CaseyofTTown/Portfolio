package module9;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CoffeeOrder {
	
	//Create the CoffeeOrder Class * holds data about coffee orders from a .txt file

	//String field for the type of coffee
	private String typeOfCoffee; 
	//int data field QuanitityOfOrder for the quantity of coffee ordered
	private int quantityOfOrder;
	//double data field CostPerCup for the price per cup
	private double costPerCup;
	//string data field temperature to indicate whether the coffee is iced or hot
	private String temperature; //will be either hot or cold
	
	public static int totalOrders = 0; //counter for total orders
	public static int totalCoffeesSold = 0;
	
	CoffeeOrder(){
		//default, probably wont be used
	}
	
	/**
	 * 
	 * @param coffeeType applies this input to typeOfCoffee
	 * @param orderQuantity applies this input to orderQuantity
	 * @param costByCup applies this input to costPerCup
	 * @param temperature applies this input to temperature, will be either hot or cold, not numerical
	 */
	CoffeeOrder(String coffeeType, int orderQuantity, double costByCup, String temperature ){
		this.typeOfCoffee = coffeeType;
		this.quantityOfOrder = orderQuantity;
		this.costPerCup = costByCup;
		this.temperature = temperature;
		CoffeeOrder.totalOrders++; //tracks total amount of orders
		CoffeeOrder.totalCoffeesSold += orderQuantity;  //tracks order quantity
	}
	

	//output all orders from the array using 
	//Coffee \t\t Quantity \t Price \t Temperature
	public void printCoffeeOrderDetails() {
		 System.out.printf("%-15s\t%-10d\t%-10.2f\t%-10s\n", typeOfCoffee, quantityOfOrder,
				 costPerCup, temperature);
	}
	public double calculateOrderCost() {
		return this.quantityOfOrder * this.costPerCup;
	}

	

	public static void main(String[] args) {
		 
	        ArrayList<CoffeeOrder> coffeeOrders = new ArrayList<>();
	        double totalCost = 0;
	        try {
	            File file = new File("src/coffe_input.txt");
	            Scanner scanner = new Scanner(file);
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine();
	                String[] parts = line.split(" ");
	                String coffeeType = parts[0];
	                int orderQuantity = Integer.parseInt(parts[1]);
	                double costByCup = Double.parseDouble(parts[2]);
	                String temperature = parts[3];
	                CoffeeOrder order = new CoffeeOrder(coffeeType, orderQuantity, costByCup, temperature);
	                coffeeOrders.add(order);
	                totalCost += order.calculateOrderCost();
	            }
	            scanner.close();
	        } catch (FileNotFoundException e) {
	            System.out.println("File not found: " + e.getMessage());
	        } catch (NumberFormatException e) {
	            System.out.println("Error parsing number: " + e.getMessage());
	        }
		
		for (CoffeeOrder order : coffeeOrders) {
			order.printCoffeeOrderDetails();
		}
		
		System.out.println("Total number of orders: " + CoffeeOrder.totalOrders );
		System.out.println("The total number of coffees sold is: " + CoffeeOrder.totalCoffeesSold);
		System.out.println("The total cost of all coffees: " + totalCost);
		
	}

}
