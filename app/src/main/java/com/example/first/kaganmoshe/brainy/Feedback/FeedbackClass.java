package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.util.Log;

import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;

import java.util.ArrayList;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;

/**
 * Created by tamirkash on 6/7/15.
 */
public abstract class FeedbackClass implements IHeadSetData {
    // Graph Data members
    private ArrayList<ParcelableDataPoint> concentrationPoints;
    private int mLastX = 0;
    private int mNumOfUserPauses = 0;
    protected int finalScore;

    public FeedbackClass() {
        concentrationPoints = new ArrayList<>();
        EegHeadSet headSet = AppManager.getInstance().getHeadSet();
        headSet.registerListener(this);
    }

    public void incNumOfUserPauses() {
        mNumOfUserPauses++;
    }

    public int getNumOfUserPauses() {
        return mNumOfUserPauses;
    }

    public void stopTimerAndRecievingData() {
        AppManager.getInstance().getHeadSet().unregisterListener(this);
    }

    public void resumeTimerAndReceivingData() {
        EegHeadSet headSet = AppManager.getInstance().getHeadSet();
        headSet.registerListener(this);
    }

    public ArrayList<ParcelableDataPoint> getConcentrationPoints() {
        return concentrationPoints;
    }

    @Override
    public void onAttentionReceived(int attValue) {
        concentrationPoints.add(new ParcelableDataPoint(mLastX++, attValue));
        Log.d("FEEDBACK - onAttention", "att=" + attValue);
    }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {

    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }

    public int getLastX() {
        return mLastX;
    }

    public int getDistractionScore() {
        return 0;
    }

    public abstract int getGameScore();

    public void insertRecordToHistoryDB(Context context, String name) {
        int concentrationScore = getConcentrationScore(concentrationPoints);

        Log.d("DP", name);
        AppManager.getHistoryDBInstance(context).insertRecord(name, concentrationScore
                + getGameScore() + getDistractionScore(), concentrationScore);
    }

    public static int getConcentrationScore(ArrayList<ParcelableDataPoint> concentrationPoints) {
        int concentrationSum = 0;

        for (ParcelableDataPoint dp : concentrationPoints) {
            Log.d("FEEDBACK - getScore", "att=" + dp.getY());
            concentrationSum += dp.getY();
        }

        return concentrationSum / concentrationPoints.size();
    }
}
