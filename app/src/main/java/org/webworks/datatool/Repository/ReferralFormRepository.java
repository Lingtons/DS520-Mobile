package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.ClientForm;
import org.webworks.datatool.Utility.UtilFuns;
import java.util.ArrayList;
import java.util.Date;

public class ReferralFormRepository extends DbAdapter {

    private final String TABLE_NAME = "referral";
    private final String KEY_ID = "_id";
    private final String KEY_CLIENT_NAME = "name";
     private final String KEY_CLIENT_IDENTIFIER = "client_identifier";
    private final String KEY_CLIENT_LASTNAME = "lastname";
    private final String KEY_CLIENT_ENCODED_IMAGE = "encoded_image";


    public ReferralFormRepository(Context _context) {
        super(_context);
    }

    public long saveReferralForm(ClientForm referralForm) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();
        long saved;

        values.put(KEY_CLIENT_NAME, referralForm.getClientName());
        values.put(KEY_CLIENT_LASTNAME, referralForm.getClientLastname());

        //newly added elements
        values.put(KEY_CLIENT_IDENTIFIER, referralForm.getClientIdentifier());
        values.put(KEY_CLIENT_ENCODED_IMAGE, referralForm.getEncodedImage());

        saved = db.insert(TABLE_NAME, null, values);
        db.close();
        values.clear();
        return saved;
    }


    public long updateReferralSocialDemo(ClientForm referralForm) {
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();

            values.put(KEY_CLIENT_NAME, referralForm.getClientName());
            values.put(KEY_CLIENT_LASTNAME, referralForm.getClientLastname());
            values.put(KEY_CLIENT_ENCODED_IMAGE, referralForm.getEncodedImage());

            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public long updateReferralPretest(ClientForm referralForm) {
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();
            values.put(KEY_CLIENT_ENCODED_IMAGE, referralForm.getEncodedImage());

            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public long updateReferralResult(ClientForm referralForm) {
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();

            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public long updateReferralPostTest(ClientForm referralForm) {
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();

            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public long updateReferralRefer(ClientForm referralForm) {
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();

            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public long saveApiReferralForm(ClientForm referralForm) {
        return -1;

    }

    public long updateRiskStratification(ClientForm referralForm){
        long saved = -1;
        if (referralFormExist(referralForm.getId())) {
            SQLiteDatabase db = OpenDb();
            ContentValues values = new ContentValues();


            saved = db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(referralForm.getId())});
            db.close();
            values.clear();
        }
        return saved;
    }

    public ArrayList<ClientForm> getAllHTSForm()  {
        SQLiteDatabase db = OpenDb();
        ArrayList<ClientForm> forms = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " +
                KEY_ID + " DESC LIMIT 50", null);
         //Todo include where facility = this facility in the query statement
        if (cursor.moveToFirst()) {
            do {
                ClientForm form = new ClientForm();
                form.setId(cursor.getInt(0));
                form.setClientName(cursor.getString(1));


                form.setClientCode(cursor.getString(3));
                form.setClientLastname(cursor.getString(6));
                form.setClientIdentifier(cursor.getString(7));
                form.setEncodedImage(cursor.getString(8));
                forms.add(form);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return forms;
    }

    public ArrayList<ClientForm> getAllReferralForm(String code) {
        SQLiteDatabase db = OpenDb();
        ArrayList<ClientForm> forms = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT *  FROM "+ TABLE_NAME + " WHERE " + KEY_CLIENT_IDENTIFIER + " LIKE ? OR " + KEY_CLIENT_LASTNAME + " LIKE ? OR " + KEY_CLIENT_NAME + " LIKE ?", new String[]{"%" + code + "%", "%" + code + "%", "%" + code + "%"});
        if (cursor.moveToFirst()) {
            do {
                ClientForm form = new ClientForm();
                form.setId(cursor.getInt(0));
                form.setClientName(cursor.getString(1));
                form.setClientCode(cursor.getString(3));
                //form.setCreateDate(UtilFuns.formatDate(cursor.getString(4)));
                form.setClientLastname(cursor.getString(6));
                form.setClientIdentifier(cursor.getString(7));
                form.setEncodedImage(cursor.getString(8));

                forms.add(form);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return forms;
    }

    public ClientForm getReferralFormById(int id) {
        SQLiteDatabase db = OpenDb();
        ClientForm form = new ClientForm();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_CLIENT_NAME, KEY_CLIENT_LASTNAME, KEY_ID, KEY_CLIENT_IDENTIFIER

        }, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            form.setClientName(cursor.getString(0));
            form.setClientLastname(cursor.getString(1));
            form.setId(cursor.getInt(2));
            form.setClientIdentifier(cursor.getString(3));
            cursor.close();
        }
        db.close();
        return form;
    }

    public ArrayList<Integer> getReferralFormDetailById(int id) {
        SQLiteDatabase db = OpenDb();
        ArrayList<Integer> details = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{
                KEY_ID, KEY_ID, KEY_ID
        }, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            details.add(cursor.getInt(0));
            details.add(cursor.getInt(1));
            details.add(cursor.getInt(2));
            cursor.close();
        }
        db.close();
        return details;
    }

    public void updateUploadedFromApi(String guid, int formId) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(formId)});
    }

    public ArrayList<ClientForm> getNonePostedSampleForms()  {
        SQLiteDatabase db = OpenDb();
        ArrayList<ClientForm> forms = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                ClientForm form = new ClientForm();
                form.setId(cursor.getInt(0));
                form.setClientName(cursor.getString(1));
                form.setClientCode(cursor.getString(3));
                //form.setCreateDate(UtilFuns.formatDate(cursor.getString(4)));
                form.setClientLastname(cursor.getString(6));
                form.setClientIdentifier(cursor.getString(7));
                form.setEncodedImage(cursor.getString(8));

                forms.add(form);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return forms;
    }

    private boolean referralFormExist(int id) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor =db.query(TABLE_NAME, new String[]{KEY_ID, KEY_CLIENT_NAME},
                KEY_ID + " =? ", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        db.close();
        return false;
    }

    private boolean referralFormApiIdExist(String guid) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor =db.query(TABLE_NAME, new String[]{KEY_ID},
                KEY_ID + "=?", new String[]{guid}, null, null, null);
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
}
