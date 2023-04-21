package com.example.mobileappas2.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

// DM => Database Manager (short to simplify expressions it is in)
public class DBManager {
    Context context;
    private SQLiteDatabase database;
    private Database db;

    // Construct with the context
    public DBManager(Context c) {
        this.context = c;
    }

    // Open the database
    public DBManager open() throws SQLException {
        this.db = new Database(this.context);
        this.database = this.db.getWritableDatabase();
        return this;
    }

    // close the database
    public void close() {
        this.db.close();
    }

    /*
    Below is all the insert statements for all the different tables
    I have in my database
    */
    // Category
    public void insert(String name,
                       String description) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Category.C_NAME, name);
        contentValue.put(DBDefs.Category.C_DESCRIPTION, description);
        this.database.insert(DBDefs.Category.TABLE_NAME, null, contentValue);
    }
    // Order
    public void insert(String dateCreated,
                       String dateUpdated,
                       Integer status) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Order.C_DATE_CREATED, dateCreated);
        contentValue.put(DBDefs.Order.C_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.Order.C_STATUS, status);
        this.database.insert(DBDefs.Order.TABLE_NAME, null, contentValue);
    }
    // Product Order
    public void insert(Integer productID,
                       Integer orderID) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Product_Order.C_PRODUCT_ID, productID);
        contentValue.put(DBDefs.Product_Order.C_ORDER_ID, orderID);
        this.database.insert(DBDefs.Product_Order.TABLE_NAME, null, contentValue);
    }
    // Products
    public void insert(String name,
                       String description,
                       String dateCreated,
                       String dateUpdated,
                       Float price,
                       Float listPrice,
                       Float retailPrice,
                       Integer categoryID) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Product.C_PRODUCT_NAME, name);
        contentValue.put(DBDefs.Product.C_PRODUCT_DESCRIPTION, description);
        contentValue.put(DBDefs.Product.C_PRODUCT_DATE_CREATED, dateCreated);
        contentValue.put(DBDefs.Product.C_PRODUCT_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.Product.C_PRICE, price);
        contentValue.put(DBDefs.Product.C_LIST_PRICE, listPrice);
        contentValue.put(DBDefs.Product.C_RETAIL_PRICE, retailPrice);
        contentValue.put(DBDefs.Product.C_CATEGORY_ID, categoryID);
        this.database.insert(DBDefs.Product.TABLE_NAME, null, contentValue);
    }
    // User Order
    public void insert(Integer userID,
                       Integer orderID,
                       int IGNORE) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.User_Order.C_USER_ID, userID);
        contentValue.put(DBDefs.User_Order.C_ORDER_ID, orderID);
        this.database.insert(DBDefs.User_Order.TABLE_NAME, null, contentValue);
    }
    // User
    public void insert(String fullName,
                       String email,
                       String password,
                       String postcode,
                       String address,
                       String dateRegistered,
                       String dateUpdated,
                       Integer phoneNumber) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.User.C_FULL_NAME, fullName);
        contentValue.put(DBDefs.User.C_EMAIL_ADDRESS, email);
        contentValue.put(DBDefs.User.C_PASSWORD, password);
        contentValue.put(DBDefs.User.C_POSTCODE, postcode);
        contentValue.put(DBDefs.User.C_ADDRESS, address);
        contentValue.put(DBDefs.User.C_DATE_REGISTERED, dateRegistered);
        contentValue.put(DBDefs.User.C_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.User.C_PHONE_NUMBER, phoneNumber);
        this.database.insert(DBDefs.User.TABLE_NAME, null, contentValue);
    }

/*
// this will query the given database with the given parameters and then return the resulting cursor (values)

table: the name of the table you want to query
columns: the column names that you want returned. Don't return data that you don't need.
selection: the row data that you want returned from the columns (This is the WHERE clause.)
selectionArgs: This is substituted for the ? in the selection String above.
groupBy and having: This groups duplicate data in a column with data having certain conditions. Any unneeded parameters can be set to null.
orderBy: sort the data
limit: limit the number of results to return
 */
    public Cursor fetch(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        Cursor cursor = this.database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (cursor != null) { cursor.moveToFirst(); }
        return cursor;
    }

    /*
    Below are the update statements for each of the databases that will allow me to update data within the table
     */
    // genral Update
    public int update(String table, ContentValues contentValue, String whereClause, String[] whereArgs)
    {
        return this.database.update(table, contentValue, whereClause, whereArgs);
    }

    // Category
    public int update(String name,
                       String description,
                       Integer ID,
                      String[] whereArgs) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Category.C_NAME, name);
        contentValue.put(DBDefs.Category.C_DESCRIPTION, description);
        return this.database.update(DBDefs.Category.TABLE_NAME, contentValue, "category_id = " + ID, whereArgs);
    }
    // Order
    public int update(Integer ID,
                      String dateCreated,
                      String dateUpdated,
                      Integer status,
                      String[] whereArgs) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Order.C_DATE_CREATED, dateCreated);
        contentValue.put(DBDefs.Order.C_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.Order.C_STATUS, status);
        return this.database.update(DBDefs.Order.TABLE_NAME, contentValue, "order_id = " + ID, whereArgs);
    }
    // Product Order
    public int update(Integer ID,
                       Integer productID,
                       Integer orderID,
                      String[] whereArgs) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Product_Order.C_PRODUCT_ID, productID);
        contentValue.put(DBDefs.Product_Order.C_ORDER_ID, orderID);
        return this.database.update(DBDefs.Product_Order.TABLE_NAME, contentValue, "product_order_id = " + ID, whereArgs);
    }
    // Products
    public int update(String name,
                       String description,
                       String dateCreated,
                       String dateUpdated,
                       Float price,
                       Float listPrice,
                       Float retailPrice,
                       Integer ID,
                       Integer categoryID,
                      String[] whereArgs) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.Product.C_PRODUCT_NAME, name);
        contentValue.put(DBDefs.Product.C_PRODUCT_DESCRIPTION, description);
        contentValue.put(DBDefs.Product.C_PRODUCT_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.Product.C_PRICE, price);
        contentValue.put(DBDefs.Product.C_LIST_PRICE, listPrice);
        contentValue.put(DBDefs.Product.C_RETAIL_PRICE, retailPrice);
        contentValue.put(DBDefs.Product.C_CATEGORY_ID, categoryID);
        return this.database.update(DBDefs.Product.TABLE_NAME, contentValue, "product_id = " + ID, whereArgs);
    }
    // User Order
    public int update(Integer ID,
                       Integer userID,
                       Integer orderID,
                       String[] whereArgs,
                       int IGNORE) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.User_Order.C_USER_ID, userID);
        contentValue.put(DBDefs.User_Order.C_ORDER_ID, orderID);
        return this.database.update(DBDefs.User_Order.TABLE_NAME, contentValue, "user_order_id = " + ID, whereArgs);
    }
    // User
    public int update(String fullName,
                       String email,
                       String password,
                       String postcode,
                       String address,
                       String dateUpdated,
                       Integer ID,
                       Integer phoneNumber,
                       String[] whereArgs) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBDefs.User.C_FULL_NAME, fullName);
        contentValue.put(DBDefs.User.C_EMAIL_ADDRESS, email);
        contentValue.put(DBDefs.User.C_PASSWORD, password);
        contentValue.put(DBDefs.User.C_POSTCODE, postcode);
        contentValue.put(DBDefs.User.C_ADDRESS, address);
        contentValue.put(DBDefs.User.C_DATE_UPDATED, dateUpdated);
        contentValue.put(DBDefs.User.C_PHONE_NUMBER, phoneNumber);
        return this.database.update(DBDefs.User.TABLE_NAME, contentValue, "user_id = " + ID, whereArgs);
    }

    // Delete
    public void delete(String tableName, String IDName, String[] whereArgs) {
        this.database.delete(tableName, IDName + "=?", whereArgs);
        //                   "Product",            "ID = ?"     "String[]{1}"
    }

    public void purgeDatabase()
    {
        this.database.delete(DBDefs.Product_Order.TABLE_NAME, null, null);
        this.database.delete(DBDefs.Product.TABLE_NAME, null, null);
        this.database.delete(DBDefs.Category.TABLE_NAME, null, null);
        this.database.delete(DBDefs.User.TABLE_NAME, null, null);
        this.database.delete(DBDefs.Order.TABLE_NAME, null, null);
        this.database.delete(DBDefs.User_Order.TABLE_NAME, null, null);
    }
}
