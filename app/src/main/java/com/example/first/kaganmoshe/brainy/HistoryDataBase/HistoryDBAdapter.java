package com.example.first.kaganmoshe.brainy.HistoryDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tamirkash on 8/25/15.
 */
public class HistoryDBAdapter {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public enum ETimeRange {
        LAST_WEEK(-7, "Last Week"),
        LAST_MONTH(-30, "Last Month"),
        LAST_YEAR(-365, "Last Year");

        private int value;
        private String name;

        ETimeRange(int value, String name){
            this.value = value;
            this.name = name;
        }

        public static String getStartDate(ETimeRange timeRange){
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
            calendar.add(Calendar.DATE, timeRange.getValue());

            String startDate = DATE_FORMAT.format(calendar.getTime());

            return startDate.substring(0, startDate.indexOf(" ")) + " 00:00:00";
        }

        @Override
        public String toString(){
            return name;
        }

        public static ETimeRange enumValueOf(String name){
            ETimeRange timeRangeRes = ETimeRange.LAST_WEEK;

            for(ETimeRange timeRange : ETimeRange.values()){
                if(name.equals(timeRange.toString())){
                    timeRangeRes = timeRange;
                }
            }

            return timeRangeRes;
        }

        public int getValue() {
            return value;
        }
    }

    private final HistoryDB mHistoryDB;

    public HistoryDBAdapter(Context context) {
        mHistoryDB = new HistoryDB(context);
    }

    public boolean insertRecord(String name, int score, int concentrationAvg) {
        long success;
        ContentValues newRecord = new ContentValues();
        SQLiteDatabase db = mHistoryDB.getWritableDatabase();
        Date date = new Date();

        newRecord.put(HistoryDB.NAME, name);
        newRecord.put(HistoryDB.DATE_TIME, DATE_FORMAT.format(date));
        newRecord.put(HistoryDB.CONCENTRATION, concentrationAvg);
        newRecord.put(HistoryDB.SCORE, score);
        success = db.insert(HistoryDB.TABLE_NAME, null, newRecord);
        db.close();

        return success >= 0;
    }

    public boolean insertRecord(String name, int concentrationAvg){
        long success;
        ContentValues newRecord = new ContentValues();
        SQLiteDatabase db = mHistoryDB.getWritableDatabase();
        Date date = new Date();

        newRecord.put(HistoryDB.NAME, name);
        newRecord.put(HistoryDB.DATE_TIME, DATE_FORMAT.format(date));
        newRecord.put(HistoryDB.CONCENTRATION, concentrationAvg);
        success = db.insert(HistoryDB.TABLE_NAME, HistoryDB.SCORE, newRecord);
        db.close();

        return success >= 0;
    }

    public ArrayList<HistoryRecordData> getRecords(String name, ETimeRange timeRange) {
        String timeRangeFrom = ETimeRange.getStartDate(timeRange);
        String timeRangeTo = DATE_FORMAT.format(new Date());

        String[] selectionArgs = {name, timeRangeFrom, timeRangeTo};
        String selection = HistoryDB.NAME + " =? AND " + HistoryDB.DATE_TIME + " BETWEEN ? AND ?";
        String[] columns = {HistoryDB.NAME, HistoryDB.DATE_TIME, HistoryDB.SCORE, HistoryDB.CONCENTRATION};

        return getRecords(selectionArgs, selection, columns);
    }

    public ArrayList<HistoryRecordData> getRecordsWithoutOne(String name, ETimeRange timeRange){
        String timeRangeFrom = ETimeRange.getStartDate(timeRange);
        String timeRangeTo = DATE_FORMAT.format(new Date());

        String[] selectionArgs = {timeRangeFrom, timeRangeTo};
        String selection = HistoryDB.DATE_TIME + " BETWEEN ? AND ? AND " + HistoryDB.NAME + " != " + name;
        String[] columns = {HistoryDB.NAME, HistoryDB.DATE_TIME, HistoryDB.CONCENTRATION};

        return getRecords(selectionArgs, selection, columns);
    }

    private ArrayList<HistoryRecordData> getRecords(String[] selectionArgs, String selection, String[] columns){
        SQLiteDatabase db = mHistoryDB.getWritableDatabase();
        ArrayList<HistoryRecordData> records = new ArrayList<>();

        Cursor cursor = db.query(HistoryDB.TABLE_NAME, columns, selection,
                selectionArgs, null, null, HistoryDB.DATE_TIME + " ASC");

        while(cursor.moveToNext()){
            HistoryRecordData record = new HistoryRecordData();

            for(String column : columns){
                if(cursor.isNull(cursor.getColumnIndex(column))){
                    continue;
                }

                record.addAttribute(cursor.getString(cursor.getColumnIndex(column)), column);
            }

            records.add(record);
        }

        db.close();
        cursor.close();

        return records;
    }

    public static class HistoryDB extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "HISTORY_DB";
        private static final String TABLE_NAME = "HISTORY_TABLE";
        private static final int DATABASE_VERSION = 1;
        private static final String UID = "_id";
        public static final String NAME = "Name";
        public static final String DATE_TIME = "Time";
        public static final String SCORE = "Score";
        public static final String CONCENTRATION = "Concentration";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
                + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
                + " VARCHAR(50), " + DATE_TIME + " VARCHAR(50), "
                + SCORE + " VARCHAR(50), "
                + CONCENTRATION + " VARCHAR(50));";

        public HistoryDB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
