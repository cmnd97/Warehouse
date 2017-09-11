package com.cmnd97.warehouse.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cristi-mnd on 28.08.17.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "inventory.db";
    public final static int DB_VERSION = 1;
    private static final String[] projection = {
            StockContract.StockEntry._ID,
            StockContract.StockEntry.COLUMN_NAME,
            StockContract.StockEntry.COLUMN_PRICE,
            StockContract.StockEntry.COLUMN_QUANTITY,
            StockContract.StockEntry.COLUMN_SUPPLIER_NAME,
            StockContract.StockEntry.COLUMN_SUPPLIER_PHONE,
            StockContract.StockEntry.COLUMN_SUPPLIER_EMAIL,
            StockContract.StockEntry.COLUMN_IMAGE
    };
    SQLiteDatabase currentDb;

    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StockContract.StockEntry.CREATE_TABLE_STOCK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertItem(StockItem item) {
        currentDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_NAME, item.getProductName());
        values.put(StockContract.StockEntry.COLUMN_PRICE, item.getPrice());
        values.put(StockContract.StockEntry.COLUMN_QUANTITY, item.getQuantity());
        values.put(StockContract.StockEntry.COLUMN_SUPPLIER_NAME, item.getSupplierName());
        values.put(StockContract.StockEntry.COLUMN_SUPPLIER_PHONE, item.getSupplierPhone());
        values.put(StockContract.StockEntry.COLUMN_SUPPLIER_EMAIL, item.getSupplierEmail());
        values.put(StockContract.StockEntry.COLUMN_IMAGE, item.getImage());
        long id = currentDb.insert(StockContract.StockEntry.TABLE_NAME, null, values);
    }

    public Cursor readStock(String queryText) {

        currentDb = getReadableDatabase();

        //   ability to search without writing the exact word
        String selection = StockContract.StockEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + queryText + "%"};

        if (!queryText.equals(""))

            return currentDb.query(
                    StockContract.StockEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
        else
            return currentDb.query(
                    StockContract.StockEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
    }

    public Cursor readItem(long itemId) {
        currentDb = getReadableDatabase();
        String selection = StockContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        return currentDb.query(
                StockContract.StockEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }

    public void updateItem(long currentItemId, int quantity) {
        currentDb = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_QUANTITY, quantity);
        String selection = StockContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(currentItemId)};
        currentDb.update(StockContract.StockEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }

    public void sellOneItem(long itemId, int quantity) {
        currentDb = getWritableDatabase();
        int newQuantity = 0;
        if (quantity > 0) {
            newQuantity = quantity - 1;
        }
        ContentValues values = new ContentValues();
        values.put(StockContract.StockEntry.COLUMN_QUANTITY, newQuantity);
        String selection = StockContract.StockEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(itemId)};
        currentDb.update(StockContract.StockEntry.TABLE_NAME,
                values, selection, selectionArgs);
    }
}
