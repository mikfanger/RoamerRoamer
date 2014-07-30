package com.example.roamer.events;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ModelMyEvents {

    public static ArrayList<ItemMyEvents> ItemsMyEvents;

    public static void LoadModel(ArrayList<ItemMyEvents> loadArray) {
    	ItemsMyEvents = new ArrayList<ItemMyEvents>();
    	
		 
    	
		if (loadArray != null){
			
			if (loadArray.size() != 0){
				for(ItemMyEvents item : loadArray) {
			           ItemsMyEvents.add(item);
			        } 
			}
			
		}
    	
    }

    public static ItemMyEvents GetbyId(int id){

        for(ItemMyEvents item : ItemsMyEvents) {
            if (item.Id == id) {
                return item;
            }
        }
        return null;
    }
    

}