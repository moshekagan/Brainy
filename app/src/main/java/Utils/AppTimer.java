package Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tamirkash on 8/19/15.
 */
public class AppTimer extends AppTime {
    public interface IAppTimerListener {
        void onTimeTick(String timeString);

        void onTimeFinish(String timeString);
    }

    private List<IAppTimerListener> listeners = new LinkedList<>();
    private long totalTime;

    // Methods
    public AppTimer(int totalTimeInSeconds, ETimeStringFormat format) {
        super(format);
        currTime = totalTime = totalTimeInSeconds * 1000;
    }

    public AppTimer(long totalTimeInMilSec, ETimeStringFormat format) {
        super(format);
        currTime = totalTime = totalTimeInMilSec;
    }

    public void registerListener(IAppTimerListener listener) {
        this.listeners.add(listener);
    }

    public void unregisterListener(IAppTimerListener listener) {
        if (listeners.contains(listener))
            listeners.remove(listener);
    }

    public void resetTimer() {
        currTime = totalTime;
    }

    @Override
    protected void updateTimer() {
        if (timerOn) {
            if (currTime > 0) {
                for (IAppTimerListener appListener : listeners) {
                    if (appListener != null) {
                        Logs.debug("TIMER", "ON TIME TICK" + this.toString());
                        currTime -= 1000;
                        appListener.onTimeTick(this.toString());
                    }
                }
                handler.postDelayed(timer, 1000);
            } else {
                for (IAppTimerListener appListener : listeners) {
                    if (appListener != null) {
                        Logs.debug("TIMER", "ON TIME FINISH" + this.toString());
                        appListener.onTimeFinish(this.toString());
                    }
                }
                timerOn = false;
            }
        }
    }
}
