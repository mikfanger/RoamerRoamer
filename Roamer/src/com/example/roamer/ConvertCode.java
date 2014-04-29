package com.example.roamer;

public class ConvertCode{
	

	public ConvertCode(){
		
	}
	


public static String convertLocation(int location){
	
	String curLocation = "";
	
	   switch(location){
	   	case 0:
	   		curLocation ="Not Selected";
	   		break;
	   	case 1:
	   		curLocation ="Boston";
	           break;
	   	case 2:
	   		curLocation ="San Francisco";
	   		break;
	   	case 3:
	   		curLocation ="Las Vegas";
	   		break;
	   	case 4:
	   		curLocation ="New York";
	   		break;
	   	case 5:
	   		curLocation ="Los Angeles";
	   		break;
	   	case 6:
	   		curLocation ="Houston";
	   		break;
	   	case 7:
	   		curLocation ="Philadelphia";
	   		break;
	   	case 8:
	   		curLocation ="Phoenix";
	   		break;
	   	case 9:
	   		curLocation ="San Antonio";
	   		break;
	   	case 10:
	   		curLocation ="San Diego";
	   		break;
	   	case 11:
	   		curLocation ="Dallas";
	   		break;
	   	case 12:
	   		curLocation ="San Jose";
	   		break;
	   	case 13:
	   		curLocation ="Austin";
	   		break;
	   	case 14:
	   		curLocation ="Jacksonville";
	   		break;
	   	case 15:
	   		curLocation ="Indianapolis";
	   		break;
	   	case 16:
	   		curLocation ="Seattle";
	   		break;
	   	case 17:
	   		curLocation ="Dever";
	   		break;
	   	case 18:
	   		curLocation ="Washington DC";
	   		break;
	   	case 19:
	   		curLocation ="Chicago";
	   		break;
	   }
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