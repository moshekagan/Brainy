package com.example.first.kaganmoshe.brainy;

import com.example.first.kaganmoshe.brainy.Setting.AppSettings;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import EEG.MindWave;
import Utils.Logs;

/**
 * Created by kaganmoshe on 5/30/15.
 */
public class AppManager implements IHeadSetData {
    // Data Members
    private static final String APP_MANAGER = "AppManager";
    private static AppManager m_Instance;
    private EegHeadSet m_HeadSet;
    private AppSettings m_AppSettings;

    // C'tor
    private AppManager(){
        m_AppSettings = new AppSettings();

        if (m_AppSettings.isUsingHeadSet()){
            if (m_AppSettings.getHeadSetType() == EHeadSetType.MindWave){
                m_HeadSet = new MindWave();
            }
        }

        connectToHeadSet();
    }

    public static AppManager getInstance(){
        if (m_Instance == null){
            m_Instance = new AppManager();
            Logs.info(APP_MANAGER, "~ * ~ * ~ * Just created AppManager instance (Singleton)! * ~ * ~ * ~");
        }

        return m_Instance;
    }

    // Methods
    public EegHeadSet getHeadSet(){
        return m_HeadSet;
    }

    public AppSettings getAppSettings() { return m_AppSettings; }

    public void connectToHeadSet(){
        if (m_AppSettings.isUsingHeadSet() && m_HeadSet != null){
            m_HeadSet.connect();
            Logs.info(APP_MANAGER, "Try to connect to MindWave");
        }
    }

    @Override
    public void onAttentionReceived(int attValue) { /* Do Nothing! */ }

    @Override
    public void onMeditationReceived(int medValue) { /* Do Nothing! */ }


    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        // While the HeadSet is not connected we try to connect
        switch (connectionState) {
            case DEVICE_CONNECTED:
            case DEVICE_CONNECTING:
            case BLUETOOTH_NOT_AVAILABLE:
                break;
            case DEVICE_NOT_FOUND:
            case DEVICE_NOT_CONNECTED:
                connectToHeadSet();
                Logs.info(APP_MANAGER, "Connection fail from some reason, try to connect again...");
                break;
        }
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) { /* Do Nothing! */ }
}
