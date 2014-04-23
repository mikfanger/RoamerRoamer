package com.example.roamer.profilelist;

public class Item {

    public int Id;
    public byte[] IconFile;
    public String Name;
    public String Location;
    public boolean Sex;
    public String StartDate;
    public int Industry;

    public Item(int id, byte[] iconFile, String name, String location, boolean sex, String startDate, int industry) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        Location = location;
        Sex = sex;
        StartDate = startDate;
        Industry = industry;

    }

}