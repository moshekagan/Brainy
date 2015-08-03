package com.example.first.kaganmoshe.brainy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.first.kaganmoshe.brainy.CustomActivity.CustomActivity;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import EEG.EHeadSetType;
import Utils.Logs;


public class SettingsActivity extends CustomActivity {

    final private String SETTINGS_ACTIVITY = "Settings Activity";

    private RadioButton m_MindWaveRadionBtn;
    private RadioButton m_DemoRadionBtn;
    private RadioButton m_EmotivRadionBtn;
    private RadioButton m_EnglishRadionBtn;
    private RadioButton m_HebrewRadionBtn;
    private RadioGroup m_HeadSetRadioGroup;
    private RadioGroup m_LenguageRadioGroup;

    private Button m_Skip;
    private Button m_Connect;
    private BetterSpinner headsetSpinner;
    EHeadSetType headSetType = EHeadSetType.Moker; // Default is Moker

    private static final String[] HEADSETS = new String[]{
            "MindWave", "Emotiv", "Demo"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.setOnBackPressedActivity(LoginActivity.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, HEADSETS);

        headsetSpinner = (BetterSpinner) findViewById(R.id.headset_list);
        m_Connect = (Button) findViewById(R.id.connectButton);
        m_Skip = (Button) findViewById(R.id.skipButton);

        headsetSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String headSetSelection = parent.getItemAtPosition(position).toString();
                headSetType = (headSetSelection.equals("MindWave")) ? EHeadSetType.MindWave : EHeadSetType.Moker;
                Log.d("HEADSET_TYPE", headSetSelection);
                headsetSpinner.onItemClick(parent, view, position, id);

            }
        });
        headsetSpinner.setAdapter(adapter);

        setTouchNClick(R.id.headset_list);
        setTouchNClick(R.id.connectButton);
        setTouchNClick(R.id.skipButton);

        initViewActivity();
    }


    private void initViewActivity() {
        m_Connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onDoneClick();
            }
        });
//
//        m_DemoRadionBtn = (RadioButton) findViewById(R.id.DemoRadioButton);
//        m_MindWaveRadionBtn = (RadioButton) findViewById(R.id.MindWaveRadioButton);
//        m_EmotivRadionBtn = (RadioButton) findViewById(R.id.EmotivRadioButton);
//
//        m_EnglishRadionBtn = (RadioButton) findViewById(R.id.englishRadioButton);
//        m_HebrewRadionBtn = (RadioButton) findViewById(R.id.hebrewRadioButton);
//
//        m_HeadSetRadioGroup = (RadioGroup) findViewById(R.id.headSetRadioGroup);
//        m_LenguageRadioGroup = (RadioGroup) findViewById(R.id.lengugeRadioGroup);
//
//        m_DemoRadionBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {onHeadSetRadioButtonsClick(v);}
//        });
//        m_MindWaveRadionBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {onHeadSetRadioButtonsClick(v);}
//        });
//        m_EmotivRadionBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {onHeadSetRadioButtonsClick(v);}
//        });
//
//        m_MindWaveRadionBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {onLanguageRadioButtonsClick(v);}
//        });
//        m_MindWaveRadionBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {onLanguageRadioButtonsClick(v);}
//        });
    }

    private void onLanguageRadioButtonsClick(View v) {
        m_LenguageRadioGroup.clearCheck();
        m_EnglishRadionBtn.setChecked(true);
        m_HebrewRadionBtn.setChecked(false);
    }

    private void onHeadSetRadioButtonsClick(View v) {
//        m_HeadSetRadioGroup.clearCheck();
//
//        switch (v.getId()){
//            case R.id.EmotivRadioButton:
//                m_DemoRadionBtn.setChecked(true);
////                m_EmotivRadionBtn.setChecked(false);
////                m_MindWaveRadionBtn.setChecked(false);
//                break;
//            case R.id.DemoRadioButton:
//                m_DemoRadionBtn.setChecked(true);
////                m_EmotivRadionBtn.setChecked(false);
////                m_MindWaveRadionBtn.setChecked(false);
//                break;
//            case R.id.MindWaveRadioButton:
//                m_MindWaveRadionBtn.setChecked(true);
////                m_DemoRadionBtn.setChecked(false);
////                m_EmotivRadionBtn.setChecked(false);
//                break;
//        }
    }

    private void updateHeadSetType() {
//        if (m_MindWaveRadionBtn.isChecked()){
//            headSetType = EHeadSetType.MindWave;
//        } else if (m_DemoRadionBtn.isChecked()){
//            headSetType = EHeadSetType.Moker;
//        } else if (m_EmotivRadionBtn.isChecked()){
//            headSetType = EHeadSetType.Moker;
//        }

        Logs.info(SETTINGS_ACTIVITY, "Set Headset to: " + headSetType.toString());
        AppManager.getInstance().getAppSettings().setHeadSetType(headSetType);
    }

    private void onDoneClick() {
        updateHeadSetType();
        AppManager.getInstance().configureAndConnectHeadSet();

        //TODO - Update settings
//        Intent intent = new Intent(this, MenuActivity.class);
//        intent.putExtra("Message", "");
//        startActivity(intent);
        Utils.startNewActivity(this, MenuActivity.class);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_setting, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
