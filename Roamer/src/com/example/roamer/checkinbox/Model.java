package com.example.roamer.checkinbox;

import java.util.ArrayList;

public class Model {

	
    public static ArrayList<Item> Items;
    public static ArrayList<Item> allItems;
   // public static final String fields[] = {DatabaseSetup.colName};
    

    public static void LoadModel(ArrayList<Item> loadArray) {

        Items = new ArrayList<Item>();
        allItems = new ArrayList<Item>();
        
        for(Item item : loadArray) {
	           allItems.add(item);
	        } 
        
        for (Item item: allItems){        	

        		int idTemp = item.Id;
        		String iconTemp = item.IconFile;
        		String nameTemp = item.Name;
        		String dateTemp = item.Date;
        		
        		Items.add(new Item (idTemp,iconTemp,nameTemp,dateTemp));
        }
        
        if (Items.size()<1)
        {
        	Items.add(new Item (1,"none", "none", "none"));
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