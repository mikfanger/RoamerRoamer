package com.example.roamer.checkinbox;

public class Item {

    public int Id;
    public byte[] IconFile;
    public String Name;
    public String Date;

    public Item(int id, byte[] iconFile, String name, String date) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        Date = date;

    }

}