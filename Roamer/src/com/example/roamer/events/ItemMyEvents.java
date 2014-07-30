package com.example.roamer.events;

public class ItemMyEvents {

	public int Id;
	//public int RowId;
    public byte[] IconFile;
    public String Time;
    public String Date;
    public String EventType;
    public String Host;
    public String Attend;
    public String Location;
    public String Description;
    public String EventId;

    public ItemMyEvents(int id, byte[] iconFile, String date, 
    		String eventtype, String host, String attend, String desc, String location, String eventId, String time) {

        Id = id;
        IconFile = iconFile;
        Host = host;
        Date = date;
        EventType = eventtype;
        Attend = attend;
        Location = location;
        Description = desc;
        EventId = eventId;
        Time = time;

    }

}