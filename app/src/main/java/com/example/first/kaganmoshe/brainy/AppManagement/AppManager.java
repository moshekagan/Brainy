package com.example.first.kaganmoshe.brainy.AppManagement;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
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
    private static AudioManager mAudioManager;
    private static boolean mMusicMutedByUser = false;
    private static boolean mMusicMutedByApp = false;
    private static Context mContext;
    private static HistoryDBAdapter mHistoryDB;
    private final static GamesManager mGamesManager = new GamesManager();
    private EegHeadSet mHeadSet;
    private AppSettings mAppSettings;

    private static int mActivitiesOpened = 0;

    // C'tor
    private AppManager() {
        mAppSettings = new AppSettings();
        Logs.info("APP_MANAGER", "APP_MANAGER");
    }

    public static HistoryDBAdapter getHistoryDBInstance(Context context) {
        if (mHistoryDB == null) {
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

    public void configureHeadSet() {
        EHeadSetType type = getAppSettings().getHeadSetType();
        if (mAppSettings.isUsingHeadSet() /*&& mHeadSet == null*/ && mAppSettings.isChangedHeadSetType()) {
            if (mHeadSet != null) {
                mHeadSet.close();
                mHeadSet = null;
            }
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

    public void setBackgroundMusic(Context context) {
        if (mBackgroundMusic == null) {
            mContext = context;
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(
                    new AudioManager.OnAudioFocusChangeListener() {
                        @Override
                        public void onAudioFocusChange(int focusChange) {
                            AppManager.getInstance().onAudioFocusChange(focusChange);
                        }
                    },
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            initBackgroundMusic();
        }
    }

    private void initBackgroundMusic() {
        mBackgroundMusic = MediaPlayer.create(mContext, R.raw.background_music);
        mBackgroundMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mBackgroundMusic.setLooping(true);
    }

    public void playBackgroundMusic() {
        if (mBackgroundMusic == null) {
            initBackgroundMusic();
        }
        if (!mBackgroundMusic.isPlaying() && !mMusicMutedByUser && !mMusicMutedByApp) {
            mBackgroundMusic.start();
        }
    }

    public void pauseBackgroundMusic() {
        mBackgroundMusic.pause();
    }

    public void muteMusicForUserRequest(boolean muteMode) {
        if (mMusicMutedByUser && !muteMode && !mMusicMutedByApp) {
            mMusicMutedByUser = muteMode;
            playBackgroundMusic();
        } else if (!mMusicMutedByUser && muteMode && !mMusicMutedByApp) {
            mBackgroundMusic.stop();
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }

        mMusicMutedByUser = muteMode;
    }

    public void muteMusicForAppRequest(boolean muteMode) {
        if (mMusicMutedByApp && !muteMode && !mMusicMutedByUser) {
            mMusicMutedByApp = muteMode;
            playBackgroundMusic();
        } else if (!mMusicMutedByApp && muteMode && !mMusicMutedByUser) {
            mBackgroundMusic.stop();
            mBackgroundMusic.release();
            mBackgroundMusic = null;
        }

        mMusicMutedByApp = muteMode;
    }

    public GamesManager getGamesManager() {
        return mGamesManager;
    }

    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mBackgroundMusic == null) initBackgroundMusic();
                else if (!mBackgroundMusic.isPlaying()) playBackgroundMusic();
                mBackgroundMusic.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mBackgroundMusic.isPlaying()) mBackgroundMusic.stop();
                mBackgroundMusic.release();
                mBackgroundMusic = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mBackgroundMusic.isPlaying()) mBackgroundMusic.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mBackgroundMusic.isPlaying()) mBackgroundMusic.setVolume(0.1f, 0.1f);
                break;
        }
    }

    public void onActivityStopped() {
        if (--mActivitiesOpened == 0) {
            pauseBackgroundMusic();
        }
        Log.d("APP_MANAGER", "onActivityStopped");
    }

    public void onActivityStarted(boolean playMusic) {
        muteMusicForAppRequest(!playMusic);

        if (++mActivitiesOpened > 0) {
            playBackgroundMusic();
        }
        Log.d("APP_MANAGER", "onActivityStarted");
    }

    public AudioManager getAudioManager() {
        return mAudioManager;
    }
}
