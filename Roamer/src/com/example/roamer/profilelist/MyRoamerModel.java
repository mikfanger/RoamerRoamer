package com.example.roamer.profilelist;

import java.util.ArrayList;

public class MyRoamerModel {

	
    public static ArrayList<MyRoamerItem> Items;
    public static ArrayList<MyRoamerItem> allItems;
   // public static final String fields[] = {DatabaseSetup.colName};
    

    public static void LoadModel(ArrayList<MyRoamerItem> loadArray) {

        Items = new ArrayList<MyRoamerItem>();
        allItems = new ArrayList<MyRoamerItem>();
        
        for(MyRoamerItem item : loadArray) {
	           allItems.add(item);
	        } 
        
        for (MyRoamerItem item: allItems){        	
        		
        		
        		System.out.println("Roamer in model is: "+item);
        		int idTemp = item.Id;
        		byte[] iconTemp = item.IconFile;
        		String nameTemp = item.Name;
        		String locTemp = item.Location;
        		String sexTemp = item.Sex;
        		String dateTemp = item.StartDate;
        		String jobTemp = item.Job;
        		String industryTemp = item.Industry;
        		String travelTemp = item.Travel;
        		String airTemp = item.Air;
        		String startTemp = item.StartDate;
        		
        		Items.add(new MyRoamerItem (idTemp,iconTemp,
        				nameTemp,locTemp,sexTemp,dateTemp,airTemp,jobTemp,travelTemp,industryTemp,startTemp));
        }
        
    }

    public static MyRoamerItem GetbyId(int id){

        for(MyRoamerItem item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
    


}