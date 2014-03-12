package com.example.roamer.profilelist;

import java.util.ArrayList;


public class Model {

	
    public static ArrayList<Item> Items;
    public static ArrayList<Item> allItems;

    public static void LoadModel(String location) {

        Items = new ArrayList<Item>();
        allItems = new ArrayList<Item>();
        
        /*
        DatabaseSetup.init(this);
        for (int i = 0; i<100; i++)
        {
        	Database
        	Items.add(newItem())
        	
        }
        */
        
        
        allItems.add(new Item(1, "pic1.png", "George", "Boston",1));
        allItems.add(new Item(2, "pic2.png", "Steve","Boston",1));
        allItems.add(new Item(3, "pic3.png", "Sandy","Boston",1));
        allItems.add(new Item(4, "pic4.png", "Roy","Boston",1));
        allItems.add(new Item(5, "pic5.png", "Alabaster", "Boston",1));
        allItems.add(new Item(6, "pic4.png", "Amanda","Boston",0));
        allItems.add(new Item(7, "pic5.png", "Alabaster","Boston",1));
        

        for (Item item: allItems){        	
        	if (item.Location.equals(location))
        	{
        		int idTemp = item.Id;
        		String iconTemp = item.IconFile;
        		String nameTemp = item.Name;
        		String locTemp = item.Location;
        		int sexTemp = item.Sex;
        		
        		Items.add(new Item (idTemp,iconTemp,nameTemp,locTemp, sexTemp));
        	}
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