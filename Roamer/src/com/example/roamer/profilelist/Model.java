package com.example.roamer.profilelist;

import java.util.ArrayList;


public class Model {

	
    public static ArrayList<Item> Items;
    public static ArrayList<Item> allItems;

    public static void LoadModel(ArrayList<Item> roamerList) {

        Items = new ArrayList<Item>();

        if(roamerList.size() > 0){
        	
        	for (Item item: roamerList){        	

        		int idTemp = item.Id;
        		byte[] iconTemp = item.IconFile;
        		String nameTemp = item.Name;
        		String locTemp = item.Location;
        		boolean sexTemp = item.Sex;
        		String dateTemp = item.StartDate;
        		
        		System.out.println("Date during model load is: "+dateTemp);
        		
        		Items.add(new Item (idTemp,iconTemp,nameTemp,locTemp, sexTemp,dateTemp));
        	}
        }       
    }

    public static Item GetbyId(int id){

        for(Item item : Items) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
    
}