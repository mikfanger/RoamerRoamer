package com.roamer;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

// TODO: Auto-generated Javadoc
/**
 * The Class ConvertCode.
 */
public class ConvertCode{
	

	/**
	 * Instantiates a new convert code.
	 */
	public ConvertCode(){
		
	}
	


/**
 * Convert location.
 *
 * @param location the location
 * @return the string
 */
public static String convertLocation(int location){
	
	String curLocation = "";
	
	ArrayList<String> locations = new ArrayList<String>();
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Cities");
	query.whereNotEqualTo("Code", 1000);

	try {
		List<ParseObject> cityList = query.find();
		
		int i = 0;
		while (i < cityList.size()){
			locations.add(cityList.get(i).getString("Name"));
			i++;
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if (locations.size() > 0) {
		curLocation = locations.get(location);
	}
	return curLocation;
	
}

/**
 * Conver sex.
 *
 * @param sex the sex
 * @return the string
 */
public static String converSex(int sex){
	
	String stringSex = "";
	
	if(sex == 0){
		System.out.println("Sex of roamer is: "+sex);
		stringSex = "male";
	}
	else{
		stringSex = "female";
	}
	
	return stringSex;
}

/**
 * Convert from sex.
 *
 * @param sex the sex
 * @return the int
 */
public static int convertFromSex(String sex){
	
	int stringSex = 0;
	
	switch(sex){
	
	case "male":
		stringSex = 1;
	}

	return stringSex;
}

/**
 * Convert industry.
 *
 * @param industry the industry
 * @return the string
 */
public static String convertIndustry(int industry){
	
	String stringIndustry = "";
	
	switch(industry){
   	case 0:
   		stringIndustry ="Not Selected";
   		break;
   	case 1:
   		stringIndustry ="Agriculture";
           break;
   	case 2:
   		stringIndustry ="Accounting";
   		break;
   	case 3:
   		stringIndustry ="Advertising";
   		break;
   	case 4:
   		stringIndustry ="Aerospace & Defense";
   		break;
   	case 5:
   		stringIndustry ="Aircraft & Airline";
   		break;
   	case 6:
   		stringIndustry ="Automotive";
   		break;
   	case 7:
   		stringIndustry ="Banking & Finance";
   		break;
   	case 8:
   		stringIndustry ="Biotechnology";
   		break;
   	case 9:
   		stringIndustry ="Consumer Products";
   		break;
   	case 10:
   		stringIndustry ="Chemical";
   		break;
   	case 11:
   		stringIndustry ="Consulting";
   		break;
   	case 12:
   		stringIndustry ="Education";
   		break;
   	case 13:
   		stringIndustry ="Engineering";
   		break;
   	case 14:
   		stringIndustry ="Entertainment";
   		break;
   	case 15:
   		stringIndustry ="Government";
   		break;
   	
   	case 16:
   		stringIndustry ="Healthcare";
           break;
   	case 17:
   		stringIndustry ="Insurance";
   		break;
   	case 18:
   		stringIndustry ="Legal";
   		break;
   	case 19:
   		stringIndustry ="Marketing";
   		break;
   	case 20:
   		stringIndustry ="Medical Products";
   		break;
   	case 21:
   		stringIndustry ="Media & Design";
   		break;
   	case 22:
   		stringIndustry ="Oil & Gas";
   		break;
   	case 23:
   		stringIndustry ="Power & Utilities";
   		break;
   	case 24:
   		stringIndustry ="Recruiting";
   		break;
   	case 25:
   		stringIndustry ="Real Estate";
   		break;
   	case 26:
   		stringIndustry ="Retail & Wholesale";
   		break;
   	case 27:
   		stringIndustry ="Service";
   		break;
   	case 28:
   		stringIndustry ="Sports";
   		break;
   	case 29:
   		stringIndustry ="Student";
   		break;
   	case 30:
   		stringIndustry ="Technology";
   		break;
   	case 31:
   		stringIndustry ="Transportation";
   		break;
   	case 32:
   		stringIndustry ="Travel & Hospitality";
   		break;
	}
	
	return stringIndustry;
	
}

/**
 * Convert job.
 *
 * @param job the job
 * @return the string
 */
public static String convertJob(int job){
	
	String stringJob = "";
	
	switch(job){
   	case 0:
   		stringJob ="Not Selected";
   		break;
   	case 1:
   		stringJob ="Accountant";
           break;
   	case 2:
   		stringJob ="Marketing";
   		break;
   	case 3:
   		stringJob ="Consultant";
   		break;
   	case 4:
   		stringJob ="Lawyer";
   		break;
   	case 5:
   		stringJob ="Sales";
   		break;
   	case 6:
   		stringJob ="Doctor";
   		break;
   	case 7:
   		stringJob ="Scientist";
   		break;
	}
	return stringJob;
	
}

/**
 * Convert travel.
 *
 * @param travel the travel
 * @return the string
 */
public static String convertTravel(int travel){
	
	String stringTravel = "";
	
	switch(travel){
   	case 0:
   		stringTravel ="Not Selected";
   		break;
   	case 1:
   		stringTravel ="0%-10%   Explorer";
           break;
   	case 2:
   		stringTravel ="10%-30%  Excursionist";
   		break;
   	case 3:
   		stringTravel ="40%-60%  Wanderer";
   		break;
   	case 4:
   		stringTravel ="60%-80%  Nomad";
   		break;
   	case 5:
   		stringTravel ="80%-100%  Globetrotter";
   		break;
	}
	
	return stringTravel;
	
}

/**
 * Convert from travel.
 *
 * @param travel the travel
 * @return the int
 */
public static int convertFromTravel(String travel){
	
	int stringTravel = 0;
	
	switch(travel){
   	case "Not Selected":
   		stringTravel = 0;
   		break;
   	case "0%-10%   Explorer":
   		stringTravel = 1;
           break;
   	case "10%-30%  Excursionist":
   		stringTravel = 2;
   		break;
   	case "40%-60%  Wanderer":
   		stringTravel = 3;
   		break;
   	case "60%-80%  Nomad":
   		stringTravel = 4;
   		break;
   	case "80%-100%  Globetrotter":
   		stringTravel = 5;
   		break;
	}
	
	return stringTravel;
	
}

/**
 * Convert from location.
 *
 * @param location the location
 * @return the string
 */
public static String convertFromLocation(int location){
	
	String stringTravel = "";
	
	switch(location){

   	case 1:
   		stringTravel ="West";
           break;
   	case 2:
   		stringTravel ="Southwest";
   		break;
   	case 3:
   		stringTravel ="Midwest";
   		break;
   	case 4:
   		stringTravel ="Southeast";
   		break;
   	case 5:
   		stringTravel ="Northeast";
   		break;
	}
	
	return stringTravel;
	
}



/**
 * Convert airline.
 *
 * @param air the air
 * @return the string
 */
public static String convertAirline(int air){
	
	String stringAir = "";
	
	switch(air){
   	case 0:
   		stringAir ="Not Selected";
   		break;
   	case 1:
   		stringAir ="Frontier";
           break;
   	case 2:
   		stringAir ="Virgin";
   		break;
   	case 3:
   		stringAir ="JetBlue";
   		break;
   	case 4:
   		stringAir ="Alaska";
   		break;
   	case 5:
   		stringAir ="Southwest";
   		break;
   	case 6:
   		stringAir ="Delta";
   		break;
   	case 7:
   		stringAir ="Airtran";
   		break;
   	case 8:
   		stringAir ="US Airways";
   		break;
   	case 9:
   		stringAir ="American Airlines";
   		break;
   	case 10:
   		stringAir ="United";
   		break;
	}
	
	return stringAir;
	
}

/**
 * Convert hotel.
 *
 * @param hotel the hotel
 * @return the string
 */
public static String convertHotel(int hotel){
	
	String stringHotel = "";
	
	switch(hotel){
   	case 0:
   		stringHotel ="Not Selected";
   		break;
   	case 1:
   		stringHotel ="Hilton";
           break;
   	case 2:
   		stringHotel ="Marriott";
   		break;
   	case 3:
   		stringHotel ="Wyndham";
   		break;
   	case 4:
   		stringHotel ="Choice";
   		break;
   	case 5:
   		stringHotel ="Starwood";
   		break;
   	case 6:
   		stringHotel ="Hyatt";
   		break;
   	case 7:
   		stringHotel ="Intercontinental";
   		break;
	}
	
	
	return stringHotel;

}

/**
 * Convert type.
 *
 * @param type the type
 * @return the string
 */
public static String convertType(int type){
	
	String stringHotel = "";
	
	switch(type){

   	case 1:
   		stringHotel ="At Airport";
           break;
   	case 2:
   		stringHotel ="Concert/Festival";
   		break;
   	case 3:
   		stringHotel ="Dinner/Meal";
   		break;
   	case 4:
   		stringHotel ="Drinks";
   		break;
   	case 5:
   		stringHotel ="Professional/Seminarl";
   		break;
   	case 6:
   		stringHotel ="Sporting Event";
   		break;
	}
	
	
	return stringHotel;

}

/**
 * Convert type back.
 *
 * @param type the type
 * @return the int
 */
public static int convertTypeBack(String type){
	
	int stringType = 0;
	
	switch(type){

   	case "At Airport":
   		stringType =1;
           break;
   	case "Concert/Festival":
   		stringType =2;
   		break;
   	case "Dinner/Meal":
   		stringType =3;
   		break;
   	case "Drinks":
   		stringType =4;
   		break;
   	case "Professional/Seminar":
   		stringType =5;
   		break;
   	case "Sporting Event":
   		stringType =6;
   		break;
	}
	
	
	return stringType;
}

/**
 * Convert time back.
 *
 * @param time the time
 * @return the int
 */
public static int convertTimeBack(String time){
	
	int stringTime = 0;
	
	switch(time){

   	case "Morning: (6AM - 11:30AM)":
   		stringTime =1;
           break;
   	case "Mid-Day: (11:30AM - 1:30PM)":
   		stringTime =2;
   		break;
   	case "Evening: (5:30PM - 7:30PM)":
   		stringTime =3;
   		break;
   	case "Night: (8:30PM - 10:30PM)":
   		stringTime =4;
   		break;
   	case "Late Night: (11:30PM - 2:00AM)":
   		stringTime =5;
   		break;
	}	
	return stringTime;
}

/**
 * Convert time.
 *
 * @param time the time
 * @return the string
 */
public static String convertTime(int time){
	
	String stringHotel = "";
	
	switch(time){

	case 1:
		stringHotel = "Morning: (6AM - 11:30AM)";
		break;
   	case 2:
   		stringHotel ="Mid-Day: (11:30AM - 1:30PM)";
        break;
   	case 3:
   		stringHotel ="Evening: (5:30PM - 7:30PM)";
   		break;
   	case 4:
   		stringHotel ="Night: (8:30PM - 10:30PM)";
   		break;
   	case 5:
   		stringHotel = "Late Night: (11:30PM - 2:00AM)";
   		break;
	}
	
	
	return stringHotel;

}

}