package com.example.roamer.events;

import java.util.ArrayList;

public class Model {

    public static ArrayList<Item> Items;

    public static void LoadModel() {

        Items = new ArrayList<Item>();
        Items.add(new Item(1, "pic1.png", "You_Me", "1/12/14","Lunch","8","Lou Malnati","We will get started at 8, ask for Steve"));
        Items.add(new Item(2, "pic1.png", "John_guy", "1/12/14","Dinner","8","Lou Malnati","We will get started at 8, ask for Steve"));
        Items.add(new Item(3, "pic1.png", "Gary", "1/12/14","Dinner","8","Mcdonalds","We will get started at 8, ask for Steve"));
        Items.add(new Item(4, "pic1.png", "Reed", "1/12/14","Dinner","8","Spy Bar","We will get started at 8, ask for Steve"));
        Items.add(new Item(5, "pic1.png", "Alabaster", "1/12/14","Dinner","8","Cracked","We will get started at 8, ask for Steve"));
        Items.add(new Item(6, "pic1.png", "Steve_guy", "1/12/14","Dinner","8","Fall Dome","We will get started at 8, ask for Steve"));
        Items.add(new Item(7, "pic1.png", "John_guy", "1/12/14","Dinner","8","Wll Done","We will get started at 8, ask for Steve"));
        Items.add(new Item(8, "pic1.png", "Gary", "1/12/14","Dinner","8","McCrackers","We will get started at 8, ask for Steve"));
        Items.add(new Item(9, "pic1.png", "Reed", "1/12/14","Dinner","8","Lost and Found","We will get started at 8, ask for Steve"));
        Items.add(new Item(10, "pic1.png", "Alabaster", "1/12/14","Dinner","8","Forder","We will get started at 8, ask for Steve"));
        Items.add(new Item(11, "pic1.png", "Steve_guy", "1/12/14","Dinner","8","Conde Nast","We will get started at 8, ask for Steve"));
        Items.add(new Item(12, "pic1.png", "John_guy", "1/12/14","Dinner","8","Mortons","We will get started at 8, ask for Steve"));
        Items.add(new Item(13, "pic1.png", "Gary", "1/12/14","Dinner","8","Mortons","We will get started at 8, ask for Steve"));
        Items.add(new Item(14, "pic1.png", "Reed", "1/12/14","Dinner","8","Mortons","We will get started at 8, ask for Steve"));
        Items.add(new Item(15, "pic1.png", "Alabaster", "1/12/14","Dinner","8","Mortons","We will get started at 8, ask for Steve"));
    
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