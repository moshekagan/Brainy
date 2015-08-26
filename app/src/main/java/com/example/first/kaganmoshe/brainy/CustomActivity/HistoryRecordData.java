package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.util.Log;

import com.example.first.kaganmoshe.brainy.HistoryDBAdapter;

/**
 * Created by tamirkash on 8/26/15.
 */
public class HistoryRecordData {
    public int score;
    public int concentration;
    public String name;
    public String date;

    public HistoryRecordData(){}

    public void addAttribute(String value, String key) {
        switch(key){
            case HistoryDBAdapter.HistoryDB.NAME:
                this.name = value;
                break;
            case HistoryDBAdapter.HistoryDB.SCORE:
                this.score = Integer.valueOf(value);
                break;
            case HistoryDBAdapter.HistoryDB.CONCENTRATION:
                this.concentration = Integer.valueOf(value);
                break;
            case HistoryDBAdapter.HistoryDB.DATE_TIME:
                this.date = value.substring(8, 10) + "." + value.substring(5, 7)
                        + "." + value.substring(0, 4) + " - " + value.substring(11, 16);
                Log.d("DB", "Date: " + this.date);
                break;
        }
    }
}
