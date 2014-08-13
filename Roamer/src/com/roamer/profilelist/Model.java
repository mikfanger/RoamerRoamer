package com.roamer.profilelist;

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
        		int industryTemp = item.Industry;
        		
        		Items.add(new Item (idTemp,iconTemp,nameTemp,locTemp, sexTemp,dateTemp, industryTemp));
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