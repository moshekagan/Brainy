package EEG;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

/**
 * Created by kaganmoshe on 5/9/15.
 */
public class  MindWave extends EegHeadSet {

    // Final Members
    final boolean RAW_ENABLED = false;
    final String MIND_WAVE = "MindWave";

    // Data Members
    private static MindWave m_MindWave; // TODO - Change this to Factory pastern
    private int m_CurrentRawData;

    private BluetoothAdapter m_BluetoothAdapter;
    private TGDevice m_TgDevice;
    private TGEegPower m_TGEegPower;

    private int m_PreviousAttentionValue;
    private int m_PreviousMeditationValue;

    private Handler m_TgDeviceHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:
                    stateChangeReceived(msg.arg1);
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
                    poorSignalReceived(msg.arg1);
                case TGDevice.MSG_ATTENTION:
                    attentionReceived(msg.arg1);
                    break;
                case TGDevice.MSG_MEDITATION:
                    meditationReceived(msg.arg1);
                    break;
                case TGDevice.MSG_RAW_DATA:
                    rawDataReceived(msg.arg1);
                    break;
                case TGDevice.MSG_EEG_POWER:
                    m_TGEegPower = (TGEegPower)msg.obj;
                    eegPowerReceived(m_TGEegPower);
                    break;
            }
        }
    };

    // C'tor
    public MindWave(){
        m_CurrentState = EConnectionState.DEVICE_NOT_CONNECTED;
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        m_TgDevice = new TGDevice(m_BluetoothAdapter, m_TgDeviceHandler);
    }

    // Methods
    @Override
    public EConnectionState connect(){
        if (m_BluetoothAdapter == null){
            m_CurrentState = EConnectionState.BLUETOOTH_NOT_AVAILABLE;
        } else if (!IsConnected()) {
            m_TgDevice.connect(RAW_ENABLED);
        }

        return m_CurrentState;
    }

    @Override
    public void close(){
        if (m_TgDevice != null){
            m_TgDevice.close();
        }
    }

    @Override
    public boolean IsConnected(){
        return m_IsConnected;
    }

    private void stateChangeReceived(int newStateValue) {
        switch (newStateValue) {
            case TGDevice.STATE_IDLE:
                break;
            case TGDevice.STATE_CONNECTING:
                m_CurrentState = EConnectionState.DEVICE_CONNECTING;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_CONNECTING);
                break;
            case TGDevice.STATE_CONNECTED:
                m_CurrentState = EConnectionState.DEVICE_CONNECTED;
                m_IsConnected = true;
                m_TgDevice.start(); // To start receiving data
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_CONNECTED);
                break;
            case TGDevice.STATE_DISCONNECTED:
                m_CurrentState = EConnectionState.DEVICE_NOT_CONNECTED;
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_NOT_CONNECTED);
                break;
            case TGDevice.STATE_NOT_FOUND:
                m_CurrentState = EConnectionState.DEVICE_NOT_CONNECTED;
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_NOT_FOUND);
            case TGDevice.STATE_NOT_PAIRED:
                m_CurrentState = EConnectionState.BLUETOOTH_NOT_AVAILABLE;
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.BLUETOOTH_NOT_AVAILABLE);
            default:
                break;
        }
    }

    private void rawDataReceived(int rawDataValue) {
        m_CurrentRawData = rawDataValue;
    }

    private void poorSignalReceived(int signalValue) {
        raiseOnPoorSignal(signalValue);
    }

    private void meditationReceived(int medValue) {

        if (!isTheValueChangedToZero(medValue, m_PreviousMeditationValue)){
            raiseOnMeditation(medValue);
        }

        m_PreviousMeditationValue = medValue; // Update the previous value
    }

    private void attentionReceived(int attValue) {

        if (!isTheValueChangedToZero(attValue, m_PreviousAttentionValue)){
            raiseOnAttention(attValue);
        }

        m_PreviousAttentionValue = attValue; // Update the previous value
    }

    private boolean isTheValueChangedToZero(int currentValue, int previousValue){
        boolean result = false;

        if (currentValue == 0 && previousValue != 0){
            result = true;
        }

        return result;
    }

    private void eegPowerReceived(TGEegPower tgEegPower) {
    }
}
