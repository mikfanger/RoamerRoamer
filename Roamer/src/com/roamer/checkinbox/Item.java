package com.roamer.checkinbox;

public class Item {

    public int Id;
    public byte[] IconFile;
    public String Name;
    public String Date;
    public int NewMessage;

    public Item(int id, byte[] iconFile, String name, String date, int newMessage) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        Date = date;
        NewMessage = newMessage;

    }

}