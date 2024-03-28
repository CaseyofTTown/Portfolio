//a class which defines an employee object with data fields
package module10;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Employee {

	//public data fields: firstName, lastName, department, jobTitle, emailAddress,
	//private data fields: dateOfBirth, employeeId
	public String firstName, lastName, department, jobTitle, emailAddress = "";
	private String dateOfBirth = "";
	private int employeeId = 0;
	
	//write a default constructor in case not all information is known

	Employee(){
	
	}
	//write a constructor to accept and apply known values with setter functions 

	Employee(String firstName, String lastName, String department, String jobTitle, String emailAddress,
			String dateOfBirth, int employeeId){
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setDepartment(department);
		this.setJobTitle(jobTitle);
		this.setEmailAddress(emailAddress);
		this.setDateOfBirth(dateOfBirth);
		this.setEmplId(employeeId);
		
	}
	//write simple getter/setter functions to set the provided data
	public void setFirstName(String name) {
		this.firstName = name;
	}
	public void setLastName(String name) {
		this.lastName = name;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public void setDateOfBirth(String dob) {
		this.dateOfBirth = dob;
	}
	public void setEmplId(int employeeID) {
		this.employeeId = employeeID;
	}
	
	public String getName() {
		return firstName + " " + lastName;
	}
	public String getDepartment() {
		return department;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public String getEmail() {
		return emailAddress;
	}
	public String getDOB() {
		return dateOfBirth;
	}
	public int getEmployeeID() {
		return employeeId;
	}
	//write a toString() to represent all data as a string and format
	public String toString() {
		return String.format("%-5s %-15s %-15s %-15s %-25s %-10s %15d",
				firstName,
				lastName,
				department,
				jobTitle,
				emailAddress,
				dateOfBirth,
				employeeId);
	}
	
	public static void main(String[] args) {
		ArrayList<Employee> employees = new ArrayList<>();
		//create a new file object
		File file = new File("src/input-7.txt");
		boolean debugging = false; //added because I had trouble getting the second row to load :/ 
		//read the file with the scanner object using try/catch w/ exception catching
		try (Scanner inputScanner = new Scanner(file)) {
			//for loop to iterate while input.hasNextLine()
			while(inputScanner.hasNextLine()) {

				//use "split" and delimitter "tab" to break provided information into separate string objects
				String[] parts = inputScanner.nextLine().split("\t");
				//define values/hold temporarily for creating the objects
				String firstName = parts[0];
				String lastName = parts[1];
				String department = parts[2];
				String jobTitle = parts[3];
				String emailAddress = parts[4];
				String dateOfBirth = parts[5];
				int employeeId = Integer.parseInt(parts[6]);
				
				if(debugging) {
					System.out.println(Arrays.toString(parts));
				}
				/*
				 * Had to modify the .txt file to ensure consistent spacing/tabulating 
				 * If that was not the thing that was expected, i apologize in advance
				 */
				
				//create the object now from the temp values and add to array in one step
				employees.add(new Employee(firstName, lastName, department, jobTitle, emailAddress, dateOfBirth, employeeId));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//print header
	    System.out.println("***************************");
	    System.out.println("*     Dominos Software   *");
	    System.out.println("***************************");
	    System.out.println();
	    System.out.println("Name	     	 Department      	Job Title	   	Email   	 Date of birth  	ID");
	    System.out.println("============ 	 ==========     	==========   		=====    	=============	    ====");
	    
		//print the data with enhanced for loop using the .toString we overrode  
		for (Employee employee: employees) {
			System.out.println(employee.toString());
		}
		
	}

  //to try and run this program you'll need to create a .txt file with the above format, Name, department, job title, email, date of birth, and ID. ID is an Int
  //the rest of the values are read as string values and its imperative that only 1 tab be between the items or the array will not run properly in this design
	
	
}
