package com.example.roamer.events;

import java.util.ArrayList;


public class Model {

    public static ArrayList<Item> Items;

    public static void LoadModel(ArrayList<Item> loadArray) {
    	
    	
        Items = new ArrayList<Item>();
		 
        if(loadArray.size() > 0){
        	for(Item item : loadArray) {
 	           Items.add(item);
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