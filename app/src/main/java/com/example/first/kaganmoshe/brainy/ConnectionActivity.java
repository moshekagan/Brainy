package com.example.first.kaganmoshe.brainy;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;
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
    private Button m_Skip;
    private Button m_Connect;
    private BetterSpinner headsetSpinner;
    EHeadSetType headSetType = EHeadSetType.Moker; // Default is Moker

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
                android.R.layout.simple_dropdown_item_1line, HEADSETS);

        headsetSpinner = (BetterSpinner) findViewById(R.id.showList);
        m_Connect = (Button) findViewById(R.id.connectButton);
        m_Skip = (Button) findViewById(R.id.skipButton);

        headsetSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String headSetSelection = parent.getItemAtPosition(position).toString();
                switch (headSetSelection) {
                    case MINDWAVE_STR:
                        headSetType = EHeadSetType.MindWave;
                        break;
                    case EMOTIV_SRT:
                        Utils.showToastMessage(getApplicationContext(), EMOTIV_SRT + " unsupported yet, choose other option.");
                    case MOCKER_STR:
                        headSetType = EHeadSetType.Moker;
                        break;
                }

                headSetType = (headSetSelection.equals("MindWave")) ? EHeadSetType.MindWave : EHeadSetType.Moker;
                Logs.debug("HEADSET_TYPE", headSetSelection);
                headsetSpinner.onItemClick(parent, view, position, id);

            }
        });
        headsetSpinner.setAdapter(adapter);

        setTouchNClick(R.id.showList);
        setTouchNClick(R.id.connectButton);
        setTouchNClick(R.id.skipButton);

        initViewActivity();

        HistoryDBAdapter db = AppManager.getHistoryDBInstance(getApplicationContext());
        db.insertRecord("Guess The Number", 50, 89);
        db.insertRecord("Guess The Number", 60, 22);
        db.insertRecord("Guess The Number", 70, 23);
        db.insertRecord("Guess The Number", 60, 76);
        db.insertRecord("Guess The Number", 32, 45);
        db.insertRecord("Guess The Number", 15, 20);

        AppManager.getInstance().setBackgroundMusic(getApplicationContext());
        AppManager.getInstance().playBackgroundMusic();
    }

    private void initViewActivity() {
        m_Connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDoneClick();
            }
        });
    }


    private void updateHeadSetType() {
        Logs.debug(SETTINGS_ACTIVITY, "Set Headset to: " + headSetType.toString());
        AppManager.getInstance().getAppSettings().setHeadSetType(headSetType);
    }

    private void onDoneClick() {
        updateHeadSetType();
        AppManager.getInstance().configureAndConnectHeadSet();

        // TODO - Update settings
        Utils.startNewActivity(this, GamesActivity.class);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
