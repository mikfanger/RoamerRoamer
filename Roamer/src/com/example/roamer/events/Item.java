package com.example.roamer.events;

public class Item {

	public int Id;
    public String IconFile;
    public String Date;
    public String EventType;
    public String Host;
    public String Attend;
    public String Location;
    public String Description;

    public Item(int id, String iconFile, String host, 
    		String date, String eventtype, String attend, String location, String desc) {

        Id = id;
        IconFile = iconFile;
        Host = host;
        Date = date;
        EventType = eventtype;
        Attend = attend;
        Location = location;
        Description = desc;

    }

}