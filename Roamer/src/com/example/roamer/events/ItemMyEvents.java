package com.example.roamer.events;

public class ItemMyEvents {

	public int Id;
	public int RowId;
    public String IconFile;
    public String Date;
    public String EventType;
    public String Host;
    public String Attend;
    public String Location;
    public String Description;

    public ItemMyEvents(int id, int rowId, String iconFile, String date, 
    		String eventtype, String host, String attend, String desc, String location) {

        Id = id;
        RowId = rowId;
        IconFile = iconFile;
        Host = host;
        Date = date;
        EventType = eventtype;
        Attend = attend;
        Location = location;
        Description = desc;

    }

}