package com.example.roamer.profilelist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * usage: 
 * DatabaseSetup.init(egActivityOrContext);
 * DatabaseSetup.createEntry() or DatabaseSetup.getContactNames() or DatabaseSetup.getDb()
 * DatabaseSetup.deactivate() then job done
 */

class DatabaseSetup extends SQLiteOpenHelper {
static DatabaseSetup instance = null;
static SQLiteDatabase db = null;

public static void init(Context context) {
    if (null == instance) {
        instance = new DatabaseSetup(context);
    }
}

public static SQLiteDatabase getDb() {
    if (null == db) {
        db = instance.getWritableDatabase();
    }
    return db;
}

public static void deactivate() {
    if (null != db && db.isOpen()) {
        db.close();
    }
    db = null;
    instance = null;
}

public static long createEntry(String name, String mail, String phone1,
        String phone2, String address, String notes) {
    ContentValues cv = new ContentValues();
    cv.put(colName, name);
    cv.put(colMail, mail);
    cv.put(colPhone1, phone1);
    cv.put(colPhone2, phone2);
    cv.put(colAddress, address);
    cv.put(colNotes, notes);
    return getDb().insert(contactsTable, null, cv);

}

public static Cursor getContactNames() {
    // TODO Auto-generated method stub
    String[] columns = new String[] { "_id", colName };
    return getDb().query(contactsTable, columns, null, null, null, null,
            null);
}

private DatabaseSetup(Context context) {
    super(context, dbName, null, dbVersion);
}

@Override
public void onCreate(SQLiteDatabase db) {
    // TODO Auto-generated method stub
    db.execSQL("CREATE TABLE IF NOT EXISTS " + contactsTable
            + " (_id integer primary key autoincrement, " + colName
            + " TEXT NOT NULL, " + colMail + " TEXT NOT NULL, " + colPhone1
            + " TEXT NOT NULL, " + colPhone2 + " TEXT NOT NULL, "
            + colAddress + " TEXT NOT NULL, " + colNotes
            + " TEXT NOT NULL)");

    db.execSQL("CREATE TABLE IF NOT EXISTS " + templatesTable
            + " (_id integer primary key autoincrement, " + colSubject
            + " TEXT NOT NULL, " + colBody + " TEXT NOT NULL)");

    db.execSQL("CREATE TABLE IF NOT EXISTS " + tagsTable
            + " (_id integer primary key autoincrement, " + colTagName
            + " TEXT NOT NULL, " + colContact + " TEXT NOT NULL)");

}

@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + contactsTable);
    db.execSQL("DROP TABLE IF EXISTS " + templatesTable);
    db.execSQL("DROP TABLE IF EXISTS " + tagsTable);
    onCreate(db);
}

static final String dbName = "DB";
static final int dbVersion = 1;
static final String contactsTable = "Contacts";
static final String colName = "Name";
static final String colMail = "Email";
static final String colPhone1 = "Phone1";
static final String colPhone2 = "Phone2";
static final String colAddress = "Address";
static final String colNotes = "Notes";

static final String templatesTable = "Templates";
static final String colSubject = "Subject";
static final String colBody = "Body";

static final String tagsTable = "Tags";
static final String colTagName = "Name";
static final String colContact = "Contact";

}