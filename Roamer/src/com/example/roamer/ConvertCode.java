package com.example.roamer;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ConvertCode{
	

	public ConvertCode(){
		
	}
	


public static String convertLocation(int location){
	
	String curLocation = "";
	
	ArrayList<String>locations = new ArrayList();
	ParseQuery<ParseObject> query = ParseQuery.getQuery("Cities");
	query.whereNotEqualTo("Code", 0);

	try {
		List<ParseObject> cityList = query.find();
		
		int i = 0;
		locations.add("Not Selected");
		
		while (i < cityList.size()){
			locations.add(cityList.get(i).getString("Name"));
			i++;
		}
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	curLocation = locations.get(location);
	return curLocation;
	
}

public static String converSex(int sex){
	
	String stringSex = "";
	
	if(sex == 1){
		stringSex = "male";
	}
	else{
		stringSex = "female";
	}
	
	return stringSex;
}

public static String convertIndustry(int industry){
	
	String stringIndustry = "";
	
	switch(industry){
   	case 0:
   		stringIndustry ="Not Selected";
   		break;
   	case 1:
   		stringIndustry ="Aerospace/Defense";
           break;
   	case 2:
   		stringIndustry ="Automotive";
   		break;
   	case 3:
   		stringIndustry ="Banking";
   		break;
   	case 4:
   		stringIndustry ="Consumer";
   		break;
   	case 5:
   		stringIndustry ="Insurance";
   		break;
   	case 6:
   		stringIndustry ="Media & Design";
   		break;
   	case 7:
   		stringIndustry ="Oil & Gas";
   		break;
   	case 8:
   		stringIndustry ="Power & Utilities";
   		break;
   	case 9:
   		stringIndustry ="Real Estate";
   		break;
   	case 10:
   		stringIndustry ="Government";
   		break;
   	case 11:
   		stringIndustry ="Student";
   		break;
   	case 12:
   		stringIndustry ="Travel/Hospitality";
   		break;
   	case 13:
   		stringIndustry ="Information Technology";
   		break;
	}
	
	return stringIndustry;
	
}

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
   		stringTravel ="60%-80%  Nomad";
   		break;
	}
	
	return stringTravel;
	
}



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

public static String convertTime(int time){
	
	String stringHotel = "";
	
	switch(time){

	case 1:
		stringHotel = "Morning";
		break;
   	case 2:
   		stringHotel ="Mid-day";
        break;
   	case 3:
   		stringHotel ="Evening";
   		break;
   	case 4:
   		stringHotel ="Night";
   		break;
   	case 5:
   		stringHotel = "Late Night";
   		break;
	}
	
	
	return stringHotel;

}

}