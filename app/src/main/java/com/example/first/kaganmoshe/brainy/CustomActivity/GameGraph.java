package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.app.Activity;

import com.example.first.kaganmoshe.brainy.AppManager;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.IHeadSetData;

/**
 * Created by tamirkash on 8/15/15.
 */
public class GameGraph implements IHeadSetData {
    private GraphView graphView;
    private final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    private int lastXGraphAtt = 0;
    private Activity listener;

    public GameGraph(GraphView graphView, Activity listener){
        this.graphView = graphView;
        this.listener = listener;
        initGraph();
    }

    private void initGraph() {
        graphView.addSeries(series);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(2);
        gridLabelRenderer.setNumVerticalLabels(3);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);
    }

    public GraphView getGraphView() {
        return graphView;
    }

    public int getLastXGraphAtt() {
        return lastXGraphAtt;
    }

    public LineGraphSeries<DataPoint> getSeries() {
        return series;
    }
    public void stopReceivingData() {
        AppManager.getInstance().getHeadSet().unregisterListener(this);
    }

    public void resumeReceivingData() {
        AppManager.getInstance().getHeadSet().registerListener(this);
    }

    @Override
    public void onAttentionReceived(final int attValue) {
        listener.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                series.appendData(new DataPoint(lastXGraphAtt++, attValue), true, 20);
            }
        });
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
}
