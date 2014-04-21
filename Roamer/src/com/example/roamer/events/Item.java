package com.example.roamer.events;

public class Item {

	public int Id;
    public byte[] IconFile;
    public String Date;
    public String Time;
    public String EventType;
    public String Host;
    public String Attend;
    public String Location;
    public String Description;
    public String ObjectId;

    public Item(int id, byte[] iconFile, String host, 
    		String date, String eventtype, String attend, String location, String desc, String objectId, String time) {

        Id = id;
        IconFile = iconFile;
        Host = host;
        Date = date;
        EventType = eventtype;
        Attend = attend;
        Location = location;
        Description = desc;
        ObjectId = objectId;
        Time = time;

    }

}