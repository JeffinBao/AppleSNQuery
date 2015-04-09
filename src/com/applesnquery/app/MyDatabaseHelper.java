package com.applesnquery.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	
	public static final String CREATE_SEARCH_HISTORY="create table search_history ("
			+ "id integer primary key autoincrement, "
			+ "sn text, "
			+ "time text)";
	
	private Context mContext;
	
	public MyDatabaseHelper(Context context,String name,CursorFactory factory,int version){
		super(context,name,factory,version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SEARCH_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
