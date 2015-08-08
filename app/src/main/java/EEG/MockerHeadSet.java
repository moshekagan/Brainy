package EEG;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tamirkash on 6/14/15.
 */
public class MockerHeadSet extends EegHeadSet {
    private EConnectionState state = EConnectionState.DEVICE_CONNECTED;
    public Random rand = new Random();

    public MockerHeadSet() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int randomNum = rand.nextInt((50) + 1) + 50;
                // What you want to do goes here
                for (IHeadSetData headSetData : m_HeadSetData) {
                    if (headSetData != null){
                        headSetData.onAttentionReceived(randomNum);
                    }
                }
            }
        }, 0, 1000);
    }

    @Override
    public EConnectionState connect() {
        return state;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean IsConnected() {
        return true;
    }
}