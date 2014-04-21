package com.example.roamer.checkinbox;

public class ItemRequest {

    public int Id;
    public byte[] IconFile;
    public String Name;
    public String StartDate;
    public String Sex;
    public String Travel;
    public String Industry;
    public String Hotel;
    public String Job;
    public String Airline;
    public String Location;

    public ItemRequest(int id, byte[] iconFile, String name,
    		String date, String sex, String travel, String industry,
    		String hotel, String job, String location, String airline ) {

        Id = id;
        IconFile = iconFile;
        Name = name;
        StartDate = date;
        Sex = sex;
        Travel = travel;
        Industry = industry;
        Hotel = hotel;
        Job = job;
        Location = location;
        Airline = airline;
        

    }

}