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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private final String GRAPH_FRAGMENT = "Graph_Fragment";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

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

    public void startTimer() {
//        sessionTimeStart = Calendar.getInstance().getTimeInMillis();
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
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                addEntry(attValue);
//                Logs.debug(GRAPH_FRAGMENT, "Append to graph happen with value : " + attValue);
//            }
//        });
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
//        int score;
//
//        switch (mNumOfUserPauses) {
//            case 0:
//                score = 150;
//                break;
//            case 1:
//                score = 100;
//                break;
//            case 2:
//                score = 80;
//                break;
//            default:
//                score = 60;
//                break;
//        }

        return 0;
    }

//    public int getGameScore() {
//        return 100;
//    }

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
