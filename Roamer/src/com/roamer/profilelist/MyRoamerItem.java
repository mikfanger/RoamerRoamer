package com.roamer.profilelist;

public class MyRoamerItem {

    public int Id;
    public byte[] IconFile;
    public String Name;
    public String Location;
    public String Sex;
    public String StartDate;
    public String Job;
    public String Travel;
    public String Industry;
    public String Air;
    public String Hotel;
    public String Origin;
    

    public MyRoamerItem(int id, byte[] iconFile, String name,
    		String location, String sex, String startDate, String air, String job, String travel, String industry, String hotel, String origin) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        Location = location;
        Sex = sex;
        StartDate = startDate;
        Air = air;
        Job = job;
        Travel = travel;
        Industry = industry;
        Hotel = hotel;
        Origin = origin;

    }

}