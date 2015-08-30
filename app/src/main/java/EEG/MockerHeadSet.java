package EEG;

import android.os.Handler;
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
    final String MOCKER = "Mocker";
    int stam = 0;
    private EConnectionState state = EConnectionState.DEVICE_CONNECTED;
    public Random rand = new Random();

    public MockerHeadSet() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int randomNum = rand.nextInt(51) + 50;
                // What you want to do goes here
                for (IHeadSetData headSetData : m_HeadSetData) {
                    if (headSetData != null){
                        headSetData.onAttentionReceived(randomNum);
//                        if (stam++ < 3){
//                            raiseOnHeadSetChangedState(MOCKER, m_CurrentState);
//                        }
                    }
                }
            }
        }, 0, 1000);

        Runnable notConnectedTimer = new Runnable() {
            @Override
            public void run() {
                raiseOnHeadSetChangedState(MOCKER, EConnectionState.DEVICE_NOT_CONNECTED);
            }
        };

        Handler handler = new Handler();
//        handler.postDelayed(notConnectedTimer, 5000);

        Runnable connectedTimer = new Runnable() {
            @Override
            public void run() {
                raiseOnHeadSetChangedState(MOCKER, EConnectionState.DEVICE_CONNECTED);
            }
        };

        handler.postDelayed(connectedTimer, 1000);
    }

    @Override
    public EConnectionState connect() {
        m_CurrentState = EConnectionState.DEVICE_CONNECTING; // TODO - Change it
        raiseOnHeadSetChangedState(MOCKER, EConnectionState.DEVICE_CONNECTING);
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
