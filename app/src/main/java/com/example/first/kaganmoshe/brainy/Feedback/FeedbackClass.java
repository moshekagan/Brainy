package com.example.first.kaganmoshe.brainy.Feedback;

import android.text.format.DateUtils;

import com.example.first.kaganmoshe.brainy.AppManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
//    private static final Random RANDOM = new Random();
    private ArrayList<ParcelableDataPoint> concentrationPoints;
    //    private int lastXGraphAtt = 0;
//    private GraphView m_GraphView;
    private long sessionTimeStart = 0;
    private long sessionTimeStop = 0;
    private int lastX = 0;


    public FeedbackClass() {
        concentrationPoints = new ArrayList<>();
        EegHeadSet headSet = AppManager.getInstance().getHeadSet();
        headSet.registerListener(this);
    }

    public void startTimer(){
        sessionTimeStart = Calendar.getInstance().getTimeInMillis();
    }

    public void stopTimerAndRecievingData(){
        sessionTimeStop = Calendar.getInstance().getTimeInMillis();
        EegHeadSet headSet = AppManager.getInstance().getHeadSet();
        headSet.unregisterListener(this);
    }

    public void resumeRecievingData(){
        EegHeadSet headSet = AppManager.getInstance().getHeadSet();
        headSet.registerListener(this);
    }

    public long getSessionTimeInSeconds(){
        return (sessionTimeStop - sessionTimeStart) / DateUtils.SECOND_IN_MILLIS;
    }

    public long getSessionTimeInMinutes(){
        return (sessionTimeStop - sessionTimeStart) / DateUtils.MINUTE_IN_MILLIS;
    }

    public ArrayList<ParcelableDataPoint> getConcentrationPoints() {
        return concentrationPoints;
    }

    public List<ParcelableDataPoint> getConcentrationPoints(int index){
        return concentrationPoints.subList(index, concentrationPoints.size());
    }

    //    private void addEntry(int value) {
//        concentrationPoints.appendData(new DataPoint(lastXGraphAtt++, value), true, 20);
//    }

    @Override
    public void onAttentionReceived(int attValue) {
        concentrationPoints.add(new ParcelableDataPoint(lastX++, attValue));
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
        return lastX;
    }
}
