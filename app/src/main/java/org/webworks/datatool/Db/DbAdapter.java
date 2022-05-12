package org.webworks.datatool.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter {

    private static DbManager dbManager;
    private Context context;

    //TODO create individual section in their individual tables
    private static final String CREATE_REFERRAL_TABLE = "CREATE TABLE referral (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT, code TEXT, create_date DATE, update_date DATE, lastname TEXT, client_identifier TEXT, encoded_image TEXT)";
    private static final String CREATE_FACILITY_TABLE = "CREATE TABLE facilities (_id INTEGER PRIMARY KEY AUTOINCREMENT, facility_id INTEGER, datim_code TEXT, facility_name TEXT, lga_code TEXT)";
    private static final String CREATE_USER_TABLE = "CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, guid TEXT, email TEXT, facility_guid TEXT, session_expired INTEGER, password TEXT, state TEXT, lga TEXT)";
    private static final String CREATE_REPORT_TABLE = "CREATE TABLE report (_id INTEGER PRIMARY KEY AUTOINCREMENT, month INTEGER, year INTEGER, reports TEXT, uploaded INTEGER, guid TEXT, create_date DATE, update_date DATE)";
    private static final String CREATE_UPDATE_TABLE = "CREATE TABLE appupdate (_id INTEGER PRIMARY KEY AUTOINCREMENT, update_code INTEGER, update_version TEXT, update_date TEXT, last_check DATE)";
    private static final String CREATE_FINGERPRINTS_TABLE = "CREATE TABLE fingerprints (_id INTEGER PRIMARY KEY AUTOINCREMENT, fp_client_identifier TEXT, finger_position TEXT, finger_print_capture TEXT, capture_quality INTERGER)";

    public DbAdapter(Context _context) {
        context = _context.getApplicationContext();
    }

    protected SQLiteDatabase OpenDb() {
        if (dbManager == null) {
            dbManager = new DbManager(context);
        }
        return dbManager.getWritableDatabase();
    }

    public static class DbManager extends SQLiteOpenHelper {
        private static String DATABASE_NAME = "SYS_DB";
        private static int DATABASE_VERSION = 1;
        public DbManager(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_REFERRAL_TABLE);
            db.execSQL(CREATE_FACILITY_TABLE);
            db.execSQL(CREATE_USER_TABLE);
            db.execSQL(CREATE_REPORT_TABLE);
            db.execSQL(CREATE_UPDATE_TABLE);
            db.execSQL(CREATE_FINGERPRINTS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(CREATE_FINGERPRINTS_TABLE);
        }
    }
}
