package com.example.myrssreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class RSSDatabaseAdapter {
	public static final String KEY_SOURCE_ID = "_id";
	public static final String KEY_SOURCE_URL = "url";
	public static final String KEY_SOURCE_NAME = "name";
	public static final String KEY_ITEM_ID = "_id";
	public static final String KEY_ITEM_LINK = "link";
	public static final String KEY_ITEM_TITLE = "title";
	public static final String KEY_ITEM_PUBDATE = "pubDate";
	public static final String KEY_ITEM_DESCRIPTION = "description";
	public static final String KEY_ITEM_SOURCE = "source";
	public static final String KEY_ITEM_ISREADED = "isReaded";
	public static final String TABLE_SOURCE = "rss_source";
	public static final String TABLE_ITEM = "rss_item";
	
    private Context context;
    private SQLiteDatabase database;
    private RSSDatabaseHelper dbHelper;

    public RSSDatabaseAdapter(Context context) {
        this.context = context;
    }
    
    public void open() {
    	dbHelper = new RSSDatabaseHelper(context);
    	database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    	dbHelper.close();
    }
    
    public Cursor fetchSources(){
    	return database.query(false, TABLE_SOURCE, new String[] {KEY_SOURCE_ID, KEY_SOURCE_NAME, KEY_SOURCE_URL},null , null, null, null, null, null);
    }
    
    public Cursor fetchUnreadedItems(String source_id) {
    	return database.rawQuery("select " + KEY_ITEM_ID + ", " + KEY_ITEM_TITLE + ", " + KEY_ITEM_LINK + ", " + KEY_ITEM_PUBDATE + 
    			", " +  KEY_ITEM_DESCRIPTION + " from " + TABLE_ITEM + " where " + KEY_ITEM_SOURCE + "=" + source_id + " and " + 
    			KEY_ITEM_ISREADED + "=0" + " order by datetime(" + KEY_ITEM_PUBDATE + ") DESC", null);
    }
    
    public Cursor fetchAllItems(String source_id) {
    	return database.rawQuery("select " + KEY_ITEM_ID + ", " + KEY_ITEM_TITLE + ", " + KEY_ITEM_LINK + ", " + KEY_ITEM_PUBDATE + 
    			", " +  KEY_ITEM_DESCRIPTION + " from " + TABLE_ITEM + " where " + KEY_ITEM_SOURCE + "=" + source_id  + " order by datetime(" + KEY_ITEM_PUBDATE + ") DESC", null);
    }
    
    public void addNewSource(String name, String url){
    	ContentValues values = new ContentValues();
    	values.put(KEY_SOURCE_NAME, name);
    	values.put(KEY_SOURCE_URL, url);
    	try {
    		database.insertOrThrow(TABLE_SOURCE, null, values);
		} catch (SQLException e) {
			Toast toast = Toast.makeText(context, "Can't add new source", 1);
			toast.show();
		} 
    }
    
    public String getURLbyID(String source_id) {
    	Cursor cursor = database.rawQuery("select " + KEY_SOURCE_URL + 
    			" from " + TABLE_SOURCE + " where " + KEY_SOURCE_ID + 
    			"=" + source_id, null);
    	cursor.moveToFirst();
    	return cursor.getString(cursor.getColumnIndex(KEY_SOURCE_URL));
    }
    
    public String getSourceNamebyID(String source_id) {
    	Cursor cursor = database.rawQuery("select " + KEY_SOURCE_URL + 
    			" from " + TABLE_SOURCE + " where " + KEY_SOURCE_ID + 
    			"=" + source_id, null);
    	cursor.moveToFirst();
    	return cursor.getString(cursor.getColumnIndex(KEY_SOURCE_NAME));
    }
    
    public void addNewItem(String link, String title, String pubDate, 
    		String description, String source_id) {
    	ContentValues values = new ContentValues();
    	values.put(KEY_ITEM_LINK, link);
    	values.put(KEY_ITEM_PUBDATE, pubDate);
    	values.put(KEY_ITEM_SOURCE, source_id);
    	values.put(KEY_ITEM_TITLE, title);
    	values.put(KEY_ITEM_DESCRIPTION, description);
    	try {
    		database.insertOrThrow(TABLE_ITEM, null, values);
    	} catch (SQLException e) {
    		
    	} 
    }
    
    public void setItemAsReaded(String item_id) {
    	ContentValues values = new ContentValues();
    	values.put(KEY_ITEM_ISREADED, 1);
    	database.update(TABLE_ITEM, values, KEY_ITEM_ID + "=" + item_id, null);
    }

}
