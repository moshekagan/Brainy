package EEG;

import android.os.Handler;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tamirkash on 6/14/15.
 */
public class MockerHeadSet extends EegHeadSet {
    final String MOCKER = "Mocker";
    private EConnectionState state = EConnectionState.DEVICE_CONNECTED;
    public Random rand = new Random();

    public MockerHeadSet() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int randomNum = rand.nextInt(51) + 50;
                for (IHeadSetData headSetData : m_HeadSetData) {
                    if (headSetData != null){
                        headSetData.onAttentionReceived(randomNum);
                    }
                }
            }
        }, 0, 1000);

        Handler handler = new Handler();
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
        m_IsConnected = true;
        return state;
    }

    @Override
    public void close() {
        raiseOnHeadSetChangedState(MOCKER, EConnectionState.DEVICE_NOT_CONNECTED);
        m_IsConnected = false;
    }

    @Override
    public boolean IsConnected() {
        return m_IsConnected;
    }
}
