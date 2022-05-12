package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.User;


public class UserRepository extends DbAdapter {

    private static final String TABLE_NAME = "user";
    private static final String KEY_ID = "_id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_GUID = "guid";
    private static final String KEY_USER_FACILITY_GUID = "facility_guid";
    private static final String KEY_USER_SESSION_ENDED = "session_expired";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_STATE = "state";
    private static final String KEY_USER_LGA = "lga";

    public UserRepository(Context _context) {
        super(_context);
    }

    public void addUser(User user) {

        if (!userExists(user.getEmail())){
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();

            values.put(KEY_USER_EMAIL, user.getEmail());
            values.put(KEY_USER_NAME, user.getUsername());
            values.put(KEY_USER_GUID, user.getGuid());
            values.put(KEY_USER_FACILITY_GUID, user.getFacility());
            values.put(KEY_USER_PASSWORD, user.getPassword());
            values.put(KEY_USER_SESSION_ENDED, 0);
            db.insert(TABLE_NAME, null, values);
            db.close();
            values.clear();
        }
    }

    public User getUser(int id) {
        SQLiteDatabase db = OpenDb();
        User user = new User();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_USER_EMAIL, KEY_USER_NAME, KEY_USER_GUID
        }, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            user.setEmail(cursor.getString(0));
            user.setUsername(cursor.getString(1));
            user.setGuid(cursor.getString(2));
            cursor.close();
        }
        db.close();
        return user;
    }

    public String getUserFacility(String guid) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_USER_FACILITY_GUID
        }, KEY_ID + "=?", new String[]{guid}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        db.close();
        return "";
    }

    /**
     * Method checks if user exists or is session logged out
     * returns
     * 0 => user does not exists
     * 1 => user exists
     * 2=> session logged out
     * */
    public int userExists() {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + "," + KEY_USER_SESSION_ENDED + " FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(1) == 0) {
                return 1;
            }
            else {
                cursor.close();
                return 2;
            }
        }
        db.close();
        return 0;
    }

    public boolean sessionUserExists(User user) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + KEY_USER_EMAIL + " = '" +
                user.getEmail() + "' AND " + KEY_USER_PASSWORD + " = '" + user.getPassword() + "'", null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        db.close();
        return false;
    }

    public void updateUserSession(int value) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_SESSION_ENDED, value);
        db.update(TABLE_NAME, values, KEY_ID + "!=?", new String[]{String.valueOf(0)});
    }

    public boolean userSessionValid() {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT " + KEY_USER_SESSION_ENDED + " FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            if (cursor.getInt(0) == 0) {
                cursor.close();
                return true;
            }
            else {
                return false;
            }
        }
        db.close();
        return true;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = OpenDb();
        return db.query(TABLE_NAME, new String[]{
                KEY_USER_EMAIL, KEY_USER_NAME, KEY_USER_GUID
        }, null, null, null, null, null);
    }

    public boolean userExists(String email) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT " + KEY_USER_EMAIL + " FROM " + TABLE_NAME + " WHERE " + KEY_USER_EMAIL + " == '" + email + "'", null);
        if (cursor.moveToFirst()) {
            return true;
        }
        db.close();
        return false;
    }

    public void updateUserLogin(String email) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_SESSION_ENDED, 0);
        db.update(TABLE_NAME, values, KEY_USER_EMAIL + "=?", new String[]{email});
    }

    /**
     * Method logs a user out
     * */
    public void LogOutUsers() {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_SESSION_ENDED, 1);
        db.update(TABLE_NAME, values, null, null);
        db.close();
    }

}
