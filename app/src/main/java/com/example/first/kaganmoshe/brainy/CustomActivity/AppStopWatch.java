package com.example.first.kaganmoshe.brainy.CustomActivity;

import Utils.Logs;

/**
 * Created by tamirkash on 8/23/15.
 */
public class AppStopWatch extends AppTime {

    public AppStopWatch(ETimeStringFormat format) {
        super(format);
        currTime = 0;
    }

    @Override
    protected void updateTimer() {
        if (timerOn) {
            Logs.debug("TIMER", "ON TIME TICK" + this.toString());
            currTime += 1000;
            handler.postDelayed(timer, 1000);
        } else {
            timerOn = false;
        }
    }
}
