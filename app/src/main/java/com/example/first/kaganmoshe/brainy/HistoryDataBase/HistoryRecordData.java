package com.example.first.kaganmoshe.brainy.HistoryDataBase;

import android.util.Log;

import com.example.first.kaganmoshe.brainy.HistoryDataBase.HistoryDBAdapter;

import Utils.Logs;

/**
 * Created by tamirkash on 8/26/15.
 */
public class HistoryRecordData {
    public String score = "";
    public String concentration;
    public String name;
    public String date;

    public HistoryRecordData(){}

    public void addAttribute(String value, String key) {
        switch(key){
            case HistoryDBAdapter.HistoryDB.NAME:
                this.name = value;
                break;
            case HistoryDBAdapter.HistoryDB.SCORE:
                this.score = value;
                break;
            case HistoryDBAdapter.HistoryDB.CONCENTRATION:
                this.concentration = value;
                break;
            case HistoryDBAdapter.HistoryDB.DATE_TIME:
                this.date = value.substring(8, 10) + "." + value.substring(5, 7)
                        + "." + value.substring(2, 4) + " - " + value.substring(11);
                Logs.debug("DB", "Date: " + this.date);
                break;
        }
    }
}
