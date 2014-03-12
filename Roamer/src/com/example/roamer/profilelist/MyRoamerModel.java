package com.example.roamer.profilelist;

import java.util.ArrayList;

public class MyRoamerModel {

	
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
        		String locTemp = item.Location;
        		int sexTemp = item.Sex;
        		
        		Items.add(new Item (idTemp,iconTemp,nameTemp,locTemp,sexTemp));
        }
        
        if (Items.size()<1)
        {
        	Items.add(new Item (1,"none", "none", "none",1));
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