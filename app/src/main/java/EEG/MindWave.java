package EEG;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;

import Utils.Logs;

/**
 * Created by kaganmoshe on 5/9/15.
 */
public class MindWave extends EegHeadSet {

    // Final Members
    final boolean RAW_ENABLED = false;
    final String MIND_WAVE = "MindWave";

    // Data Members
    private static MindWave m_MindWave; // TODO - Change this to Factory pastern
    private boolean m_IsConnected = false;
    private int m_CurrentRawData;

    private BluetoothAdapter m_BluetoothAdapter;
    private TGDevice m_TgDevice;
    private TGEegPower m_TGEegPower;
    private EConnectionState m_ConnectState;

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
    private MindWave(){
        m_ConnectState = EConnectionState.DEVICE_NOT_CONNECTED;
        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        m_TgDevice = new TGDevice(m_BluetoothAdapter, m_TgDeviceHandler);
    }

    // Methods
    @Override
    public EConnectionState connect(){
        if (m_BluetoothAdapter == null){
            m_ConnectState = EConnectionState.BLUETOOTH_NOT_AVAILABLE;
        } else if (!IsConnected()) {
            m_TgDevice.connect(RAW_ENABLED);
        }

        return m_ConnectState;
    }

    @Override
    public void close(){
        if (m_TgDevice != null){
            m_TgDevice.close();
            Logs.info(MIND_WAVE, "MindWave Closed! /n bye bye...");
        }
    }

    @Override
    public boolean IsConnected(){
        return m_IsConnected;
    }

    private void stateChangeReceived(int newStateValue) {
        switch (newStateValue) {
            case TGDevice.STATE_IDLE:
                // TODO
                break;
            case TGDevice.STATE_CONNECTING:
                Logs.info(MIND_WAVE, Logs.SEPARATOR_LINE + "Connecting..." + Logs.SEPARATOR_LINE);
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_CONNECTING);
                break;
            case TGDevice.STATE_CONNECTED:
                Logs.info(MIND_WAVE, Logs.SEPARATOR_LINE + "Connected! :)" + Logs.SEPARATOR_LINE);
                m_IsConnected = true;
                m_TgDevice.start(); // To start receiving data
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_CONNECTED);
                break;
            case TGDevice.STATE_DISCONNECTED:
                Logs.info(MIND_WAVE, Logs.SEPARATOR_LINE + "Disconnected! :(" + Logs.SEPARATOR_LINE);
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_NOT_CONNECTED);
                break;
            case TGDevice.STATE_NOT_FOUND:
                Logs.info(MIND_WAVE, Logs.SEPARATOR_LINE + "Device not found! :(" + Logs.SEPARATOR_LINE);
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.DEVICE_NOT_FOUND);
            case TGDevice.STATE_NOT_PAIRED:
                Logs.info(MIND_WAVE, Logs.SEPARATOR_LINE + "Device not paired! :(" + Logs.SEPARATOR_LINE);
                m_IsConnected = false;
                raiseOnHeadSetChangedState(MIND_WAVE, EConnectionState.BLUETOOTH_NOT_AVAILABLE);
            default:
                break;
        }
    }

    private void rawDataReceived(int rawDataValue) {
        // Logs.info(MIND_WAVE, "Raw Data: " + rawDataValue);
        m_CurrentRawData = rawDataValue;
    }

    private void poorSignalReceived(int signalValue) {
        Logs.info(MIND_WAVE, "Poor Signal" + signalValue);
        raiseOnPoorSignal(signalValue);
    }

    private void meditationReceived(int medValue) {
        Logs.info(MIND_WAVE, "Meditation: " + medValue);

        if (!isTheValueChangedToZero(medValue, m_PreviousMeditationValue)){
            raiseOnMeditation(medValue);
        }

        m_PreviousMeditationValue = medValue; // Update the previous value
    }

    private void attentionReceived(int attValue) {
        Logs.error(MIND_WAVE, "Attention: " + attValue);

        if (!isTheValueChangedToZero(attValue, m_PreviousAttentionValue)){
            raiseOnAttention(attValue);
        }

        m_PreviousAttentionValue = attValue; // Update the previous value
    }

    private boolean isTheValueChangedToZero(int currentValue, int priviousValue){
        boolean resulat = false;

        if (currentValue == 0 && priviousValue != 0){
            resulat = true;
        }

        return resulat;
    }

    private void eegPowerReceived(TGEegPower tgEegPower) {
        Logs.info(MIND_WAVE, "*** Delta: " + tgEegPower.delta);
        Logs.info(MIND_WAVE, "*** Theta: " + tgEegPower.theta);
        Logs.info(MIND_WAVE, "*** High Alpha: " + tgEegPower.highAlpha);
        Logs.info(MIND_WAVE, "*** Low Alpha: " + tgEegPower.lowAlpha);
        Logs.info(MIND_WAVE, "*** High Beta: " + tgEegPower.highBeta);
        Logs.info(MIND_WAVE, "*** Low Beta: " + tgEegPower.lowBeta);
        Logs.info(MIND_WAVE, "*** Mid Gamma: " + tgEegPower.midGamma);
        Logs.info(MIND_WAVE, "*** Low Gamma: " + tgEegPower.lowGamma);
    }

    public static EegHeadSet getInstance(EHeadSetType headSetType) throws Exception {
        if (headSetType == EHeadSetType.MindWave){
            if (m_MindWave == null){
                m_MindWave = new MindWave();
            }
        } else {
            throw new Exception("Some Wird ERROR Happend Because There is no other type excepted 'MindWave' - WE ARE IN : EegHeadSet.getInstance()");
        }

        return m_MindWave;
    }
}
