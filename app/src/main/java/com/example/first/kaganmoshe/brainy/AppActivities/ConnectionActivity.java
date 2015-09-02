package com.example.first.kaganmoshe.brainy.AppActivities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.HistoryDataBase.HistoryDBAdapter;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import com.weiwangcn.betterspinner.library.BetterSpinner;
import EEG.EHeadSetType;
import Utils.Logs;


public class ConnectionActivity extends AppActivity {

    // Const Members
    final private String SETTINGS_ACTIVITY = "Settings Activity";
    final private static String MINDWAVE_STR = "MindWave";
    final private static String MOCKER_STR = "Demo";
    final private static String EMOTIV_SRT = "Emotiv";

    // Data Members
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.app_spinner_row, HEADSETS);

        mHeadsetSpinner = (BetterSpinner) findViewById(R.id.showList);
        mConnectButton = (Button) findViewById(R.id.connectButton);
        mSkipButton = (Button) findViewById(R.id.skipButton);

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

        setTouchNClick(R.id.showList);
        setTouchNClick(R.id.connectButton);
        setTouchNClick(R.id.skipButton);

        initViewActivity();

//        View spinnerOverlay = findViewById(R.id.spinner_overlay);
//        spinnerOverlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHeadsetSpinner.performClick();
//            }
//
//        });

        HistoryDBAdapter db = AppManager.getHistoryDBInstance(getApplicationContext());
        db.insertRecord("Guess The Number", 50, 89);
        db.insertRecord("Guess The Number", 60, 22);
        db.insertRecord("Guess The Number", 70, 23);
        db.insertRecord("Guess The Number", 60, 76);
        db.insertRecord("Guess The Number", 32, 45);
        db.insertRecord("Guess The Number", 15, 20);
        db.insertRecord("Daily Practices", 60);

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
        AppManager.getInstance().configureAndConnectHeadSet();

        // TODO - Update settings
        Utils.startNewActivity(this, MainActivity.class);
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
}
