package com.example.first.kaganmoshe.brainy.AppManagement;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.first.kaganmoshe.brainy.HistoryDataBase.HistoryDBAdapter;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Setting.AppSettings;

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
    private static AppManager mInstance;
    private static MediaPlayer mBackgroundMusic;
    private static boolean mMusicMute = false;
    private static Context mContext;
    private static HistoryDBAdapter mHistoryDB;
    private static final GamesManager mGamesManager = new GamesManager();
    private EegHeadSet mHeadSet;
    private AppSettings mAppSettings;


//    private HashMap<Integer, Drawable> m_Drawables;
//
//    public static final String MENU_CONNECTION_ICON = "MENU";

    // C'tor
    private AppManager() {
        mAppSettings = new AppSettings();
        Logs.info("APP_MANAGER", "APP_MANAGER");
//        configureAndConnectHeadSet();
    }
    //
//    public Drawable getDrawable(int i//d){
//        if(!m_Drawables.containsKey(id//)){
//            m_Drawables.put(id, //co)
//      //  }
//    }
    public static HistoryDBAdapter getHistoryDBInstance(Context context){
        if(mHistoryDB == null){
            mHistoryDB = new HistoryDBAdapter(context);
        }

        return mHistoryDB;
    }

    public static AppManager getInstance() {
        if (mInstance == null) {
            mInstance = new AppManager();
            Logs.info(APP_MANAGER, "~ * ~ * ~ * Just created AppManager instance (Singleton)! * ~ * ~ * ~");
        }

        return mInstance;
    }

    // Methods
    public EegHeadSet getHeadSet() {
        return mHeadSet;
    }

    public AppSettings getAppSettings() {
        return mAppSettings;
    }

    public void configureAndConnectHeadSet() {
        EHeadSetType type = getAppSettings().getHeadSetType();
        if (mAppSettings.isUsingHeadSet() && mHeadSet == null) {
            switch (type) {
                case MindWave:
                    mHeadSet = new MindWave();
                    Logs.info(APP_MANAGER, "Connected to: MINDWAVE :)");
                    break;
                case Moker:
                    Logs.info(APP_MANAGER, "Connected to: MOCKER");
                    mHeadSet = new MockerHeadSet();
                    break;
            }
        }

        connectToHeadSet();
    }

    public void connectToHeadSet() {
        if (mAppSettings.isUsingHeadSet() && mHeadSet != null) {
            mHeadSet.connect();
            Logs.info(APP_MANAGER, "Try to connect to MindWave");
        }
        //mocker
        else {
            mHeadSet.connect();
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

    public static void setBackgroundMusic(Context context) {
        if (mBackgroundMusic == null) {
            mContext = context;
            mBackgroundMusic = MediaPlayer.create(mContext, R.raw.background_music);
        }
    }

    public static void playBackgroundMusic() {
        if (!mBackgroundMusic.isPlaying() && !mMusicMute) {
            mBackgroundMusic.start();
            mBackgroundMusic.setLooping(true);
        }
    }

    public static void stopBackgroundMusic() {
        mBackgroundMusic.stop();
    }

    public static void pauseBackgroundMusic() {
        mBackgroundMusic.pause();
    }

    public static void muteMusic(boolean muteMode) {
        if (mMusicMute && !muteMode) {
            mBackgroundMusic = MediaPlayer.create(mContext, R.raw.background_music);
            mBackgroundMusic.setLooping(true);
            mBackgroundMusic.start();
        } else if (!mMusicMute && muteMode) {
            mBackgroundMusic.reset();
        }

        mMusicMute = muteMode;
    }

    public static Context getContext() {
        return mContext;
    }

    public static GamesManager getGamesManager(){
        return mGamesManager;
    }
}
