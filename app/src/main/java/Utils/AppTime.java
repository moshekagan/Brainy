package Utils;

import android.os.Handler;

/**
 * Created by tamirkash on 8/23/15.
 */
public abstract class AppTime {

    public enum ETimeStringFormat {
        SECONDS_ONLY, MINUTES_AND_SECONDS;
    }

    // Data Members
    protected ETimeStringFormat format;
    protected Handler handler = new Handler();
    protected Runnable timer;
    protected boolean timerOn = false;
    protected long currTime;

    // Methods
    public AppTime(int totalTimeInSeconds, ETimeStringFormat format) {
        initParameters(format);
    }

    public AppTime(ETimeStringFormat format) {
        initParameters(format);
    }

    private void initParameters(ETimeStringFormat format) {
        this.format = format;

        timer = new Runnable() {
            @Override
            public void run() {
                updateTimer();
            }
        };
    }

    public void resumeTimer() {
        timerOn = true;
        handler.postDelayed(timer, 1000);
    }

    public void stopTimer() {
        handler.removeCallbacks(timer);
        timerOn = false;
    }

    protected abstract void updateTimer();

    @Override
    public String toString() {
        String timeString;
        int seconds = (int) (currTime / 1000);
        int minutes = seconds / 60;

        seconds = seconds % 60;

        switch (format) {
            case SECONDS_ONLY:
                timeString = String.format("%02d", seconds);
                break;
            case MINUTES_AND_SECONDS:
            default:
                timeString = "" + minutes + ":"
                        + String.format("%02d", seconds)/*+ String.format("%03d", milliseconds)*/;
                break;
        }

        return timeString;
    }
}
