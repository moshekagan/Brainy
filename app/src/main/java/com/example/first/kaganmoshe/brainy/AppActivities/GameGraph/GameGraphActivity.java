package com.example.first.kaganmoshe.brainy.AppActivities.GameGraph;


import android.util.Log;

import com.example.first.kaganmoshe.brainy.AppActivities.GameActivity;

public abstract class GameGraphActivity extends GameActivity {

    protected GameGraph gameGraph = null;

    @Override
    protected void stopReceivingEEGData() {
        super.stopReceivingEEGData();
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH");

        if(gameGraph != null) {
            gameGraph.stopReceivingData();
        }
    }

    @Override
    protected void resumeReceivingEEGData() {
        super.resumeReceivingEEGData();
        Log.d("GRAPH_LIFE", "RESUME_GRAPH");

        if (gameGraph != null) {
            gameGraph.resumeReceivingData();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        if(gameGraph != null){
            gameGraph.stopReceivingData();
        }
    }
}
