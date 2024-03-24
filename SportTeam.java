package module9;

import java.util.Scanner;

public class SportTeam {
	
	//Design a class named sport that contains

	//a private String field that stores the sport name
	private String sportName = "hockey";
	//a private string field that stores the team name
	private String teamName = "alpacas";
	//a private int field that stores number of players
	private int numPlayers = 0;
	//a private double field that stores the coach salary
	private double coachesSalary = 0.0;
	//the accessor and mutator fields for each field
	public String getSportName() {
		return sportName;
	}
	public String getSportTeamName() {
		return teamName;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	public double getCoachesSalary() {
		return coachesSalary;
	}
	//returns all fields, used to print 
	public String getAllFieldsAsString() {
		return "Sport Name: " + sportName + ",\nTeam Name: " + teamName + 
				",\nNumber of Players: " + numPlayers + ",\nCoach's Salary: "
				+ coachesSalary +".";
	}
	
	public void setSportName(String nameSport) {
		this.sportName = nameSport;
	}
	public void setTeamName(String nameTeam) {
		this.teamName = nameTeam;
	}
	public void setNumberOfPlayers(int numberPlayers) {
		this.numPlayers = numberPlayers;
	}
	public void setCoachesSalary(double salary) {
		this.coachesSalary = salary;
	}

	//default values for each of those, that way default constructor will initialize
	SportTeam(){
		//default constructor to use default values 
		System.out.println("New Team created with default values.");
	}
	//constructor which will accept all the private fields 
	SportTeam(String sportName, String teamName, int numberOfPlayers, double coachesSalary){
		this.sportName = sportName;
		this.teamName = teamName;
		this.numPlayers = numberOfPlayers;
		this.coachesSalary = coachesSalary;
		
		System.out.println("New Team Created!");
	}

	//Create an object and prompt the user to enter the data for the fields
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String tempSportName, tempTeamName;
		int tempNumPlayers;
		double tempCoachesSalary;
		
		System.out.println("Provide the following information and we will create a new team!");
		System.out.println("Please enter the name of the sport: ");
		tempSportName = input.nextLine();
		System.out.println("Please enter the team name: ");
		tempTeamName = input.nextLine();
		System.out.println("Please provide the number of players: ");
		tempNumPlayers = input.nextInt();
		System.out.println("Please provide the coache's Salary");
		tempCoachesSalary = input.nextDouble();
		
		SportTeam team1 = new SportTeam(tempSportName, tempTeamName, tempNumPlayers, tempCoachesSalary) {
	      
		};
		System.out.println(team1.getAllFieldsAsString());
		
	}

	//output the data to the console 

}
