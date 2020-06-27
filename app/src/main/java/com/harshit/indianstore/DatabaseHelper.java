package com.harshit.indianstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "cart";
    static final String TABLE_NAME = "ourCart";
    // id will be our column 0
    static final String PRODUCT_ID = "productId"; // column 1
    static final String SHOP_ID = "shopId";// 2
    static final String PRICE = "price";// 4
    static final String PRODUCT_NAME = "productName";// 3
    static final String QUANTITY = "quantity";// 5
    static final String Image = "image";// not set used



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME , null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT , productId TEXT , shopId TEXT , productName TEXT , price INTEGER , quantity INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String id , String shopId , String name , int price){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID , id);
        contentValues.put(SHOP_ID , shopId);
        contentValues.put(PRODUCT_NAME , name);
        contentValues.put(PRICE , price);
        contentValues.put(QUANTITY , 1);
        long result =  db.insert(TABLE_NAME , null , contentValues);
        if(result == -1l)
            return false;
        return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        return cursor;
    }

    public boolean isPresentAlready(String pId){
        SQLiteDatabase db = this.getWritableDatabase();
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + PRODUCT_ID + " = " + pId;

//        Log.d("select Query" , selectQuery);

        Cursor findEntry = db.query("ourCart", new String[] {PRODUCT_NAME}, "productId=?", new String[] { pId }, null, null, null);

        if(findEntry.getCount() == 0)
            return false;
        return true;
    }

    public boolean deleteRow(String productId){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, PRODUCT_ID + "=?", new String[]{productId});
        if(result > 0)
            return true;
        return false;
    }
}
