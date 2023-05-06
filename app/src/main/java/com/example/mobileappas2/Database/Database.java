package com.example.mobileappas2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class Database extends SQLiteOpenHelper
{
	// public variables
    public static final int DATABASE_VERSION = 10;
    public static final String DATABASE_NAME = "Database.db";
    public Context curContext;
	
	// constructor to set the context
    public Database (Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        curContext = context;
    }

	/*
		when the object is created initilse the database
	*/
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DBDefs.User.CREATE_TABLE);
        db.execSQL(DBDefs.Category.CREATE_TABLE);
        db.execSQL(DBDefs.Order.CREATE_TABLE);
        db.execSQL(DBDefs.Product.CREATE_TABLE);
        db.execSQL(DBDefs.User_Order.CREATE_TABLE);
        db.execSQL(DBDefs.Product_Order.CREATE_TABLE);
    }
	
	/*
		when the database has been updated when remove the old database and create a new one
	*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.Product_Order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.User_Order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.Product.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.Order.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.Category.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBDefs.User.TABLE_NAME);
        onCreate(db);
    }
}
