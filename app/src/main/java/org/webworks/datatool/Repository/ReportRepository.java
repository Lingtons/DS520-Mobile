package org.webworks.datatool.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.webworks.datatool.Db.DbAdapter;
import org.webworks.datatool.Model.Report;

import java.util.Date;



public class ReportRepository extends DbAdapter {

    private static final String TABLE_NAME = "report";
    private static final String KEY_ID = "_id";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR= "year";
    private static final String KEY_REPORTS = "reports";
    private static final String KEY_UPLOADED = "uploaded";
    private static final String KEY_GUID = "guid";
    private static final String KEY_CREATE_DATE = "create_date";
    private static final String KEY_UPDATE_DATE = "update_date";

    public ReportRepository(Context _context) {
        super(_context);
    }

    public long saveReport(Report report) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();
        long saved;

        values.put(KEY_MONTH, report.getMonth());
        values.put(KEY_YEAR, report.getYear());
        values.put(KEY_REPORTS, report.getReports());
        values.put(KEY_UPLOADED, 0);
        values.put(KEY_CREATE_DATE, new Date().toString());

        saved = db.insert(TABLE_NAME, null, values);
        db.close();
        values.clear();
        return saved;
    }

    public boolean reportExist(int month, int year) {
        SQLiteDatabase db = OpenDb();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + TABLE_NAME + " WHERE " + KEY_MONTH + " == " + month +
                " AND " + KEY_YEAR + " == " + year, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        db.close();
        return false;
    }

    public Report getReport(int month, int year) {
        SQLiteDatabase db = OpenDb();
        Report report = new Report();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_MONTH + " == " + month +
                " AND " + KEY_YEAR + " == " + year, null);
        if (cursor.moveToFirst()) {
            report.setId(cursor.getInt(0));
            report.setMonth(cursor.getInt(1));
            report.setYear(cursor.getInt(2));
            report.setReports(cursor.getString(3));
            report.setUploaded(cursor.getInt(4));
            report.setGuid(cursor.getString(5));
            cursor.close();
        }
        db.close();
        return report;
    }

    public void updateReport(Report report) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        values.put(KEY_REPORTS, report.getReports());
        values.put(KEY_UPDATE_DATE, new Date().toString());
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(report.getId())});
    }

    public void updateUploaded(String guid, int id) {
        SQLiteDatabase db = OpenDb();
        ContentValues values = new ContentValues();

        values.put(KEY_UPLOADED, 1);
        values.put(KEY_GUID, guid);
        values.put(KEY_UPDATE_DATE, new Date().toString());
        db.update(TABLE_NAME, values, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }
}
