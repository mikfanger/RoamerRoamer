package com.example.roamer.checkinbox;

import java.util.ArrayList;

public class ModelRequest {

	
    public static ArrayList<ItemRequest> Items;
    public static ArrayList<ItemRequest> allItems;
   
    

    public static void LoadModel(ArrayList<ItemRequest> loadArray) {

        Items = new ArrayList<ItemRequest>();
        allItems = new ArrayList<ItemRequest>();
        
        if(loadArray.size()>0){
        	for(ItemRequest item : loadArray) {
 	           allItems.add(item);
 	        } 
        }
        
        
        for (ItemRequest item: allItems){        	

        		int idTemp = item.Id;
        		byte[] iconTemp = item.IconFile;
        		String nameTemp = item.Name;
        		String dateTemp = item.StartDate;
        		String locationTemp = item.Location;
        		String hotelTemp = item.Hotel;
        		String airTemp = item.Airline;
        		String travelTemp = item.Travel;
        		String industryTemp = item.Industry;
        		String jobTemp = item.Job;
        		String sexTemp = item.Sex;
        		String credTemp = item.CredName;
        		        		
        		Items.add(new ItemRequest (idTemp,iconTemp,nameTemp,dateTemp,
        				sexTemp,travelTemp,industryTemp,hotelTemp,
        				jobTemp,locationTemp,airTemp, credTemp));
        }
        

    }

    public static ItemRequest GetbyId(int id){

        for(ItemRequest item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
}