package com.example.first.kaganmoshe.brainy.AppActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.HistoryDataBase.HistoryDBAdapter;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.IHeadSetData;
import Utils.Logs;


public class ConnectionActivity extends AppActivity implements IHeadSetData{

    // Const Members
    final private String SETTINGS_ACTIVITY = "Settings Activity";
    final private static String MINDWAVE_STR = "MindWave";
    final private static String MOCKER_STR = "Demo";
    final private static String EMOTIV_SRT = "Emotiv";

    // Data Members
    private ProgressBar mConnectProgressBar;
    private Button mSkipButton;
    private Button mConnectButton;
    private BetterSpinner mHeadsetSpinner;
    EHeadSetType mHeadSetType = EHeadSetType.Moker; // Default is Moker

    private static final String[] HEADSETS = new String[]{
            MINDWAVE_STR,
            MOCKER_STR,
            EMOTIV_SRT
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

//        this.setOnBackPressedActivity(LoginActivity.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.headsets_spinner_row, HEADSETS);

        mHeadsetSpinner = (BetterSpinner) findViewById(R.id.showList);
        mConnectButton = (Button) findViewById(R.id.connectButton);
        mSkipButton = (Button) findViewById(R.id.skipButton);
        mConnectProgressBar = (ProgressBar) findViewById(R.id.connectProgressBar);

        mConnectProgressBar.setVisibility(View.INVISIBLE);

        mHeadsetSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String headSetSelection = parent.getItemAtPosition(position).toString();
                switch (headSetSelection) {
                    case MINDWAVE_STR:
                        mHeadSetType = EHeadSetType.MindWave;
                        break;
                    case EMOTIV_SRT:
                        Utils.showToastMessage(getApplicationContext(), EMOTIV_SRT + " is not supported yet.");
                    case MOCKER_STR:
                        mHeadSetType = EHeadSetType.Moker;
                        break;
                }

                mHeadSetType = (headSetSelection.equals("MindWave")) ? EHeadSetType.MindWave : EHeadSetType.Moker;
                Logs.debug("HEADSET_TYPE", headSetSelection);
                mHeadsetSpinner.onItemClick(parent, view, position, id);

            }
        });
        mHeadsetSpinner.setAdapter(adapter);

        initViewActivity();

//        HistoryDBAdapter db = AppManager.getHistoryDBInstance(getApplicationContext());
//        db.insertRecord("Guess The Number", 150, 89);
//        db.insertRecord("Guess The Number", 150, 89);
//        db.insertRecord("Guess The Number", 150, 89);
//        db.insertRecord("Daily Practices", 60);
//
        AppManager.getInstance().setBackgroundMusic(getApplicationContext());
        AppManager.getInstance().playBackgroundMusic();
    }

    private void initViewActivity() {
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDoneClick();
            }
        });
    }


    private void updateHeadSetType() {
        Logs.debug(SETTINGS_ACTIVITY, "Set Headset to: " + mHeadSetType.toString());
        AppManager.getInstance().getAppSettings().setHeadSetType(mHeadSetType);
    }

    private void onDoneClick() {
        updateHeadSetType();

        AppManager.getInstance().configureHeadSet();
        AppManager.getInstance().getHeadSet().registerListener(this);

        if (AppManager.getInstance().getHeadSet().IsConnected()) {
            AppManager.getInstance().getHeadSet().unregisterListener(this);
            Utils.startNewActivity(this, MainActivity.class);
        }
        else {
            AppManager.getInstance().connectToHeadSet();
            mConnectProgressBar.setVisibility(View.VISIBLE);
        }
//        // TODO - Update settings
//        Utils.startNewActivity(this, MainActivity.class);
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
//        AppManager.playBackgroundMusic();
    }

    @Override
    protected void onPause(){
        super.onPause();
//        AppManager.pauseBackgroundMusic();
    }

    @Override
    public void onAttentionReceived(int attValue) {}

    @Override
    public void onMeditationReceived(int medValue) {}

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        String msg = headSetName + " ";
        boolean showMessage = true;
        switch (connectionState){
            case DEVICE_CONNECTING:
                msg = "Connecting to " + headSetName + "...";
                showMessage = false;
                break;
            case BLUETOOTH_NOT_AVAILABLE:
                msg = "Bluetooth is off or your device is not paired to: " + headSetName;
                mConnectProgressBar.setVisibility(View.INVISIBLE);
                break;
            case DEVICE_NOT_FOUND:
                msg = "Can not find " + headSetName + ", make sure the distance is not longer than 10 meters";
                mConnectProgressBar.setVisibility(View.INVISIBLE);
                break;
//            case DEVICE_NOT_FOUND:
//                mConnectProgressBar.setVisibility(View.INVISIBLE);
//                msg += "doesn't found, make sure that the distance in not longer then 10 meters";
//                break;
            case DEVICE_NOT_CONNECTED:
                msg += "is not connected :(";
                break;
            case DEVICE_CONNECTED:
                msg += "is connected :)";
                mConnectProgressBar.setVisibility(View.GONE);
                Utils.startNewActivity(this, MainActivity.class);
                break;
        }

        if (showMessage)
            showConnectionMessage(msg);
    }

    private void showConnectionMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showToastMessage(getApplicationContext(), msg);
            }
        });
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {}
}
