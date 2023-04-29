package com.example.mobileappas2.Database;

import android.provider.BaseColumns;

public final class DBDefs
{
    private DBDefs(){}
    /*
    THIS CLASS CONTAINS THE DEFINITIONS FOR ALL OF INFO
    TO CREATE ALL OF THE DIFFERENT TABLES THAT WILL BE
    USED
     */
	
	// user
    public static class User implements BaseColumns
    {
        // C_ => COLUMN
        public static final String TABLE_NAME = "user";
        public static final String C_USER_ID = "user_id";
        public static final String C_FULL_NAME = "full_name";
        public static final String C_EMAIL_ADDRESS = "email";
        public static final String C_PASSWORD = "password";
        public static final String C_DATE_REGISTERED = "date_registered";
        public static final String C_DATE_UPDATED = "date_updated";
        public static final String C_POSTCODE = "postcode";
        public static final String C_ADDRESS = "address";
        public static final String C_PHONE_NUMBER = "phone_number";
        public static final String C_HOBBIES = "hobby";

        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_FULL_NAME + " VARCHAR, " +
                C_EMAIL_ADDRESS + " VARCHAR, " +
                C_PASSWORD + " VARCHAR, " +
                C_DATE_REGISTERED + " VARCHAR, " +
                C_DATE_UPDATED + " VARCHAR, " +
                C_HOBBIES + " VARCHAR, " +
                C_POSTCODE + " VARCHAR, " +
                C_ADDRESS + " VARCHAR, " +
                C_PHONE_NUMBER + " VARCHAR);";
    }
	
	// order
    public static final class Order implements BaseColumns
    {
        public static final String TABLE_NAME = "placed_order"; // cant have "order"
        public static final String C_ORDER_ID = "order_id";
        public static final String C_DATE_CREATED = "date_created";
        public static final String C_DATE_UPDATED = "date_updated";
        public static final String C_STATUS = "status";

        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_DATE_CREATED + " INTEGER, " +
                C_DATE_UPDATED + " INTEGER, " +
                C_STATUS + " INTEGER);";
    }

	// category
    public static final class Category implements BaseColumns
    {
        public static final String TABLE_NAME = "category";
        public static final String C_CATEGORY_ID = "category_id";
        public static final String C_NAME = "name";
        public static final String C_DESCRIPTION = "description";

        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_NAME + " VARCHAR, " +
                C_DESCRIPTION + " VARCHAR);";
    }

	// product
    public static final class Product implements BaseColumns
    {
        public static final String TABLE_NAME = "product";
        public static final String C_PRODUCT_ID = "product_id";
        public static final String C_PRODUCT_NAME = "product_name";
        public static final String C_PRODUCT_DESCRIPTION = "product_description";
        public static final String C_PRICE = "price";
        public static final String C_LIST_PRICE = "list_price";
        public static final String C_RETAIL_PRICE = "retail_price";
        public static final String C_PRODUCT_DATE_CREATED = "product_date_created";
        public static final String C_PRODUCT_DATE_UPDATED = "product_date_updated";
        public static final String C_CATEGORY_ID = "category_id";

        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_PRODUCT_NAME + " VARCHAR, " +
                C_PRODUCT_DESCRIPTION + " VARCHAR, " +
                C_PRICE + " DECIMAL(100, 2), " +
                C_LIST_PRICE + " DECIMAL(100, 2), " +
                C_RETAIL_PRICE + " DECIMAL(100, 2), " +
                C_PRODUCT_DATE_CREATED + " VARCHAR, " +
                C_PRODUCT_DATE_UPDATED + " VARCHAR, " +
                C_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY (" + C_CATEGORY_ID + ") REFERENCES " + Category.TABLE_NAME + "(" + Category.C_CATEGORY_ID + "));";

    }

	// user order
    public static final class User_Order implements BaseColumns
    {
        public static final String TABLE_NAME = "user_order";
        public static final String C_USER_ORDER_ID = "user_order_id";
        public static final String C_USER_ID = "user_id";
        public static final String C_ORDER_ID = "order_id";
        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_USER_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_USER_ID + " INTEGER, " +
                C_ORDER_ID + " INTEGER, " +
                "FOREIGN KEY (" + C_USER_ID + ") REFERENCES " + User.TABLE_NAME + "(" + User.C_USER_ID + "), " +
                "FOREIGN KEY (" + C_ORDER_ID + ") REFERENCES " + Order.TABLE_NAME + "(" + Order.C_ORDER_ID + "));";

    }

	// product order
    public static final class Product_Order implements BaseColumns
    {
        public static final String TABLE_NAME = "product_order";
        public static final String C_PRODUCT_ORDER_ID = "product_order_id";
        public static final String C_PRODUCT_ID = "product_id";
        public static final String C_ORDER_ID = "order_id";

        public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " +
                C_PRODUCT_ORDER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                C_PRODUCT_ID + " INTEGER, " +
                C_ORDER_ID + " INTEGER, " +
                "FOREIGN KEY (" + C_PRODUCT_ID + ") REFERENCES " + Product.TABLE_NAME + "(" + Product.C_PRODUCT_ID + "), " +
                "FOREIGN KEY (" + C_ORDER_ID + ") REFERENCES " + Order.TABLE_NAME + "(" + Order.C_ORDER_ID + "));";
    }
}
