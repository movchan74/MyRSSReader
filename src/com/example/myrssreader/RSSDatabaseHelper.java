package com.example.myrssreader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class RSSDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "rss_database";
	private static final int DATABASE_VERSION = 5;
	
	private static final String RSS_SOURCE_CREATE = "create table if not exists rss_source ( _id integer primary key autoincrement, "
			             + "url text not null, name text not null)";
	private static final String RSS_ITEM_CREATE = "create table if not exists rss_item ( _id integer primary key autoincrement, "
            + "link text not null unique, title text not null, pubDate integer, description text not null, source integer, isReaded integer default 0, "
			+ "foreign key(source) references rss_source( _id ))";


	public RSSDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(android.database.sqlite.SQLiteDatabase db) {
		db.execSQL(RSS_SOURCE_CREATE);
		db.execSQL(RSS_ITEM_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS rss_source");
		db.execSQL("DROP TABLE IF EXISTS rss_item");
		onCreate(db);
	};

}
