package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.Update;

import java.util.Date;



public class UpdateRepository extends DbAdapter {

    private static final String TABLE_NAME = "appupdate";
    private static final String KEY_ID = "_id";
    private static final String KEY_UPDATE_CODE = "update_code";
    private static final String KEY_UPDATE_VERSION = "update_version";
    private static final String TABLE_UPDATE_DATE = "update_date";
    private static final String KEY_LASTCHECK = "last_check";

    public UpdateRepository(Context _context) {
        super(_context);
    }
    /**
    * This code truncates the update table and adds a new update row to the database
    * */
    public void addUpdate(Update update) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();
        //delete the current row
        deleteUpdate();
        //insert a new one
        values.put(KEY_UPDATE_CODE, update.getUpdateCode());
        values.put(KEY_UPDATE_VERSION, update.getUpdateVersion());
        values.put(TABLE_UPDATE_DATE, new Date().toString());
        values.put(KEY_LASTCHECK, update.getLastCheck());
        db.insert(TABLE_NAME, null, values);

        db.close();
        values.clear();
    }
    /**
    * This code gets the last check date
    * */
    public String getLastCheck() {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_LASTCHECK
        }, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        else {
            db.close();
            cursor.close();
            return "";
        }
    }
    /**
    * This code deletes the current update from the database
    * */
    private void deleteUpdate() {
        SQLiteDatabase db = OpenDb();

        db.delete(TABLE_NAME, null, null);
        //db.close();
    }
    /**
     * This code updates the last check of the current update
     * */
    public void updateLastCheck(String lastCheck) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        values.put(KEY_LASTCHECK, lastCheck);
        db.update(TABLE_NAME, values, null, null);
    }
}
