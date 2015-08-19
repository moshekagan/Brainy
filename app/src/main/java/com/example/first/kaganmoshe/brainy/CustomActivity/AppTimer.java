package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CrazyCube.CCubeFeedback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Utils.Logs;

/**
 * Created by tamirkash on 8/19/15.
 */
public class AppTimer {
    public interface IAppTimerListener {
        void onTimeTick(String timeString);

        void onTimeFinish(String timeString);
    }

    public enum ETimeStringFormat {
        SECONDS_ONLY, MINUTES_AND_SECONDS;
    }

    // Data Members
    private List<IAppTimerListener> listeners = new LinkedList<>();
    private ETimeStringFormat format;
    private Handler handler = new Handler();
    private Runnable timer;
    private boolean timerOn = false;
    private long currTime;
    private long totalTime;

    // Methods
    public AppTimer(int totalTimeInSeconds, ETimeStringFormat format) {
        initParameters(totalTimeInSeconds * 1000, format);
    }

    public AppTimer(long totalTimeInMilSec, ETimeStringFormat format) {
        initParameters(totalTimeInMilSec, format);
    }

    private void initParameters(long totalTimeInMilSec, ETimeStringFormat format) {
        this.format = format;
        currTime = totalTime = totalTimeInMilSec;

        timer = new Runnable() {
            @Override
            public void run() {
                updateTimer();
            }
        };
    }

    public void registerListener(IAppTimerListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(IAppTimerListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    public void resumeTimer() {
        timerOn = true;
        handler.postDelayed(timer, 1000);
    }

    public void resetTimer() {
        currTime = totalTime;
    }

    public void stopTimer() {
        handler.removeCallbacks(timer);
        timerOn = false;
    }

    private void updateTimer() {
        if (timerOn) {
            if (currTime > 0) {
                for(IAppTimerListener appListener : listeners){
                    if (appListener != null) {
                        Logs.debug("TIMER", "ON TIME TICK" + this.toString());
                        currTime -= 1000;
                        appListener.onTimeTick(this.toString());
                    }
                }
                handler.postDelayed(timer, 1000);
            } else {
                for(IAppTimerListener appListener : listeners){
                    if (appListener != null) {
                        Logs.debug("TIMER", "ON TIME FINISH" + this.toString());
                        appListener.onTimeFinish(this.toString());
                    }
                }
                timerOn = false;
            }
        }
    }

    @Override
    public String toString() {
        String timeString;
//                int milliseconds = (int) (updatedTime % 1000);
        int seconds = (int) (currTime / 1000);
        int minutes = seconds / 60;

        seconds = seconds % 60;

        switch (format) {
            case MINUTES_AND_SECONDS:
                timeString = String.format("%02d", seconds);
                break;
            case SECONDS_ONLY:
            default:
                timeString = "" + minutes + ":"
                        + String.format("%02d", seconds)/*+ String.format("%03d", milliseconds)*/;
                break;
        }

        return timeString;
    }
}
