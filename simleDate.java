
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyDate {

	//year month and day, month is 0 based starting w/ january
	private int year, month, day;
	//need no arg constructor for the current date (current system time)
	MyDate(){
		//default constructor
		GregorianCalendar date = new GregorianCalendar();
		year = date.get(Calendar.YEAR);
		month = date.get(Calendar.MONTH);
		day = date.get(Calendar.DAY_OF_MONTH);
	}
	//constructor for a specified elapsed time since january 1, 1970 (Unix Time)
	MyDate(long elapsedTime){
		this.setDate(elapsedTime);
	}
	//constructor for a specified date 
	MyDate(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
		}
	//getter methods for year, month, and day
	public int getYear() {
		return year;
	}
	public int getMonth() {
		return month;
	}
	public int getDay() {
		return day;
	}
	//setter function for setting time using elapsed time
	public void setDate(long elapsedTime) {
		GregorianCalendar date = new GregorianCalendar();
		date.setTimeInMillis(elapsedTime);
		//set classes values using date object
		year = date.get(Calendar.YEAR);
		month = date.get(Calendar.MONTH);
		day = date.get(Calendar.DAY_OF_MONTH);
	}
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setDay(int day) {
		this.day = day;
	}
}

//testing in main()
public class myDateTestClass {

	public static void main(String[] args) {
		//create 2 objects, 1. default, 2. the milliseconds since january 1 1970
		MyDate defaultDate = new MyDate();
		MyDate dateGivenMillis = new MyDate(34355555133101L);
		
		//print default date obj
		System.out.println("Year: " + defaultDate.getYear());
		System.out.println("Month: " + defaultDate.getMonth());
		System.out.println("Day: " + defaultDate.getDay());
		System.out.println();
		
		System.out.println("Year: " + dateGivenMillis.getYear());
		System.out.println("Month: " + dateGivenMillis.getMonth());
		System.out.println("Day: " + dateGivenMillis.getDay());


	}

}
/*
Output
Year: 2024
Month: 2
Day: 27

Year: 3058
Month: 8
Day: 7


*/
