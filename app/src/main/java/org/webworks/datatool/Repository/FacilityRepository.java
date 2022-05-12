package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.Facility;

import java.util.ArrayList;

public class FacilityRepository extends DbAdapter {
    private static final String TABLE_NAME = "facilities";
    private static final String KEY_ID = "_id";
    private static final String KEY_FACILITY_ID = "facility_id";
    private static final String KEY_NAME = "facility_name";
    private static final String KEY_DATIM_CODE = "datim_code";
    private static final String KEY_LGA_CODE = "lga_code";

    public FacilityRepository(Context _context) {
        super(_context);
    }

    public int addFacilities(ArrayList<Facility> facilities) {
        int count = 0;
        for (Facility facility: facilities) {
            ContentValues values = new ContentValues();
            values.put(KEY_FACILITY_ID,facility.getFacilityId());
            values.put(KEY_NAME,facility.getFacilityName());
            values.put(KEY_DATIM_CODE, facility.getDatimCode());
            values.put(KEY_LGA_CODE, facility.getLgaCode());

            long inserted = -1;
            if(!facilityExists(facility.getFacilityId())) {
                if(!facility.getFacilityName().equals("")) {
                    SQLiteDatabase db = OpenDb();
                    inserted = db.insert(TABLE_NAME, null, values);
                    db.close();
                    if ((int) inserted != -1) {
                        count++;
                    }
                }
            }
            values.clear();
        }
        return count;
    }

    public ArrayList<Facility> getFacilities(){
        SQLiteDatabase db = OpenDb();
        ArrayList<Facility> facilities = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT " + KEY_FACILITY_ID + ","+ KEY_NAME +"," + KEY_LGA_CODE + " FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()){
            do {
                Facility facility = new Facility();
                facility.setFacilityId(cursor.getInt(0));
                facility.setFacilityName(cursor.getString(1));
                facility.setLgaCode(cursor.getString(2));
                facilities.add(facility);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return facilities;
    }

//    private int facilityExists(int facilityId) {
//        SQLiteDatabase db = OpenDb();
//        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + KEY_FACILITY_ID + " = '" + facilityId + "'", null);
//
//        if (cursor.moveToFirst()) {
//            db.close();
//            return 1;
//        }
//        db.close();
//        return 0;
//    }

    private boolean facilityExists(int facilityId) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ KEY_NAME },
                KEY_FACILITY_ID + "=?", new String[]{String.valueOf(facilityId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            db.close();
            cursor.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }

    public String getFacilityName(int facility_id) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.query(TABLE_NAME, new String[]{ KEY_NAME },
                KEY_FACILITY_ID + "=?", new String[]{String.valueOf(facility_id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            db.close();
            return cursor.getString(0);
        }
        else {
            db.close();
            cursor.close();
            return null;
        }
    }
}
