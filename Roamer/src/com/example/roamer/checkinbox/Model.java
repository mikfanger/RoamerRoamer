package com.example.roamer.checkinbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Model {

    public static ArrayList<Item> Items;

    public static void LoadModel() throws NumberFormatException, IOException {

        Items = new ArrayList<Item>();
        
        File file = new File("messages.txt");
        
        if (file.exists())
        {
        	String wholeLine;
        	BufferedReader in = new BufferedReader(new FileReader(file));
            
            while (in.ready())
            {
            	wholeLine = in.readLine();
            	String[] lineArray  = wholeLine.split(",");
            	Items.add(new Item(Integer.parseInt(lineArray[0]),lineArray[1],lineArray[2],lineArray[3]));
            }
            in.close();
        }
        else
        {
        	PrintWriter writer = new PrintWriter("messages.txt", "UTF-8");
        	writer.close();
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