package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.FingerPrint;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FingerPrintRepository extends DbAdapter {
    private static final String TABLE_NAME = "fingerprints";
    private static final String KEY_ID = "_id";
    private static final String KEY_FINGER_POSITION = "finger_position";
    private static final String KEY_FINGER_CAPTURE = "finger_print_capture";
    private static final String KEY_CLIENT_IDENTIFIER = "fp_client_identifier";
    private static final String KEY_CAPTURE_QUALITY = "capture_quality";

    public FingerPrintRepository(Context _context) {
        super(_context);
    }

    public void saveBulkFingerprint(ArrayList<FingerPrint> fingerPrints){
        if (!hasFingerPrintCaptured(fingerPrints.get(0).getFpClientIdentifier())){
            SQLiteDatabase db = OpenDb();
            db.beginTransaction();
            try{
                ContentValues values = new ContentValues();
                for (FingerPrint fingerPrint : fingerPrints)
                {
                    values.put(KEY_FINGER_POSITION, fingerPrint.getFingerPosition());
                    values.put(KEY_FINGER_CAPTURE, fingerPrint.getFingerPrintCapture());
                    values.put(KEY_CLIENT_IDENTIFIER, fingerPrint.getFpClientIdentifier());
                    values.put(KEY_CAPTURE_QUALITY, fingerPrint.getCaptureQuality());
                    db.insert(TABLE_NAME, null, values);
                }
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }

        }
    }

    public ArrayList<String> getAllFingerPrints(){
        SQLiteDatabase db = OpenDb();
        ArrayList<String> fingerprints = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_FINGER_CAPTURE + " FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                fingerprints.add(cursor.getString(0));
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return fingerprints;
    }

    public boolean hasFingerPrintCaptured(String clientIdentifier){
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_FINGER_CAPTURE + " FROM " + TABLE_NAME + " WHERE "+ KEY_CLIENT_IDENTIFIER  + " =?", new String[]{String.valueOf(clientIdentifier)});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();

            return true;
        }
        return false;
    }

    public boolean exists(String clientIdentifier, String position){
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_ID + " FROM " + TABLE_NAME + " WHERE "+ KEY_CLIENT_IDENTIFIER  + " =? AND "+ KEY_FINGER_POSITION +"=?", new String[]{String.valueOf(clientIdentifier), String.valueOf(position)});
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return true;
        }
        return false;
    }

    public JSONObject getClientFingerPrints(String clientIdentifier){
        SQLiteDatabase db = OpenDb();
        Map<String, JsonObject> fingerprintshash = new HashMap<>();
        Cursor cursor = db.rawQuery("SELECT "+ KEY_FINGER_POSITION +","+ KEY_FINGER_CAPTURE +", "+KEY_CAPTURE_QUALITY+" FROM " + TABLE_NAME + " WHERE "+ KEY_CLIENT_IDENTIFIER  + " =?", new String[]{String.valueOf(clientIdentifier)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                JsonObject childJson = new JsonObject();
                try{
                    childJson.addProperty("capture", cursor.getString(1).replace("\n", ""));
                    childJson.addProperty("quality", cursor.getInt(2));
                    fingerprintshash.put(cursor.getString(0), childJson);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (fingerprintshash != null){
            Gson gson = new Gson();
            String json_string = gson.toJson(fingerprintshash);
            try {
                JSONObject jsonObject = new JSONObject(json_string);
                return  jsonObject;
            }catch (JSONException err){
//                Log.d("Error", err.toString());
            }

        }


        return null;
    }

}
