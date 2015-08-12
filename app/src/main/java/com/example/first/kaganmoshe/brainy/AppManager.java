package com.example.first.kaganmoshe.brainy;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.first.kaganmoshe.brainy.Setting.AppSettings;

import java.security.PublicKey;
import java.util.HashMap;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import EEG.MindWave;
import EEG.MockerHeadSet;
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

//    private HashMap<Integer, Drawable> m_Drawables;
//
//    public static final String MENU_CONNECTION_ICON = "MENU";

    // C'tor
    private AppManager(){
        m_AppSettings = new AppSettings();
        Logs.info("APP_MANAGER", "APP_MANAGER");
//        configureAndConnectHeadSet();
    }
 //
//    public Drawable getDrawable(int i//d){
//        if(!m_Drawables.containsKey(id//)){
//            m_Drawables.put(id, //co)
//      //  }
//    }

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

    public void configureAndConnectHeadSet(){
        EHeadSetType type = getAppSettings().getHeadSetType();
        if (m_AppSettings.isUsingHeadSet() && m_HeadSet == null){
            switch (type){
                case MindWave:
                    m_HeadSet = new MindWave();
                    Logs.info(APP_MANAGER, "Connected to: MINDWAVE :)");
                    break;
                case Moker:
                    Logs.info(APP_MANAGER, "Connected to: MOCKER");
                    m_HeadSet = new MockerHeadSet();
                    break;
            }
        }

        connectToHeadSet();
    }

    public void connectToHeadSet(){
        if (m_AppSettings.isUsingHeadSet() && m_HeadSet != null){
            m_HeadSet.connect();
            Logs.info(APP_MANAGER, "Try to connect to MindWave");
        }
        //mocker
        else{
            m_HeadSet.connect();
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
