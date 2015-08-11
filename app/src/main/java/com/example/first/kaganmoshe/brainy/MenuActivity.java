package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.first.kaganmoshe.brainy.CrazyCube.CCConfigActivity;
import com.example.first.kaganmoshe.brainy.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HABConfigActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HotAirBalloonGameActivity;
import com.example.first.kaganmoshe.brainy.MindShooter.MindShooterConfigActivity;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;


public class MenuActivity extends AppActivity implements IHeadSetData{

//    private TextView toolbarText;


    private static final String MENU_TOOLBAR_TEXT = "Menu";
    private static final String GUESS_THE_NUMBER_STR = "Guess The Number";
    private static final String HOT_AIR_BALLOON_STR = "HotAir Balloon";
    private static final String CRAZY_CUBE_STR = "Crazy Cube";
    private static final String MIND_SHOOTER_STR = "Mind Shooter";
    private static MenuCustomList adapter;

    private ListView list;
    private static final String[] titles = {
            GUESS_THE_NUMBER_STR,
            HOT_AIR_BALLOON_STR,
            CRAZY_CUBE_STR,
            MIND_SHOOTER_STR
    };
    private static final Integer[] imageId = {
            R.drawable.numbers,
            R.drawable.hot_air_balloon,
            R.drawable.kuku_cube,
            R.drawable.ic_kavent
    };
    private static final String[] reviews = {
            "bla",
            "bla",
            "bla",
            "bla"
    };
    private EConnectionState currnetState = EConnectionState.IDLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

//        this.setOnBackPressedActivity(SettingsActivity.class);

        if(adapter == null){
            adapter = new
                    MenuCustomList(MenuActivity.this, titles, imageId, reviews, R.layout.menu_list_row);
        }

        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Class cls = null;

                switch (titles[+position]) {
                    case GUESS_THE_NUMBER_STR:
                        cls = GuessTheNumberConfigActivity.class;
//                        onGuessTheNumberClick();
                        break;
                    case HOT_AIR_BALLOON_STR:
                        cls = HABConfigActivity.class;
//                        onHotAirBalloonClick();
                        break;
                    case CRAZY_CUBE_STR:
                        cls = CCConfigActivity.class;
//                        onCrazyCubeClick();
                        break;
                    case MIND_SHOOTER_STR:
                        cls = MindShooterConfigActivity.class;
//                        onCrazyCubeClick();
                        break;
                }

                Utils.startNewActivity((Activity) view.getContext(), cls);
            }
        });

        try{
            EegHeadSet headSet = AppManager.getInstance().getHeadSet();
            headSet.registerListener(this);
        } catch (Exception e) { // TODO - Not need to go hear never!!!!
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

//        AppManager.getInstance().getHeadSet().unregisterListener(this);
    }

    @Override
    public void onBackPressed(){
        Utils.startNewActivity(this, SettingsActivity.class);
    }


//    private Button m_CrazyCubeBtn;
//    private Button m_GuessTheNumberBtn;
//    private Button m_SettingsBtn;
//    private Button m_HotAirBalloonBtn;
//    private TextView toolbarText;
//
//    private static final String MENU_TOOLBAR_TEXT = "Menu";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu);
//
//        initViewActivity();
//    }
//
//    private void initViewActivity() {
//        toolbarText = (TextView) findViewById(R.id.toolbarText);
//        toolbarText.setText(MENU_TOOLBAR_TEXT);
//
//        m_CrazyCubeBtn = (Button) findViewById(R.id.CrazyCubeBtn);
//        m_CrazyCubeBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                onCrazyCubeClick();
//            }
//        });
//
//        m_GuessTheNumberBtn = (Button) findViewById(R.id.guessTheNumberBtn);
//        m_GuessTheNumberBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onGuessTheNumberClick();
//            }
//        });
//
//        m_SettingsBtn = (Button) findViewById(R.id.settingsBtn);
//        m_SettingsBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onSettingsClick();
//            }
//        });
//
//        m_HotAirBalloonBtn = (Button) findViewById(R.id.hotAirBalloonBtn);
//        m_HotAirBalloonBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onHotAirBalloonClick();
//            }
//        });
//    }
//
    private void onHotAirBalloonClick() {
        Intent intent = new Intent(getApplicationContext(), HotAirBalloonGameActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onCrazyCubeClick() {
        //TODO - make code reusable for every click
        Intent intent = new Intent(getApplicationContext(), CrazyCubeActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onSettingsClick() {
        //TODO - Update settings
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onGuessTheNumberClick() {
        Intent intent = new Intent(this, GuessTheNumberConfigActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    @Override
    public void onAttentionReceived(int attValue) {

    }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        currnetState = connectionState;
        String msg = headSetName + " ";

        switch (connectionState) {
            case DEVICE_CONNECTED:
                msg += "is connected :)";
                break;
            case DEVICE_CONNECTING:
                msg += "is connecting...";
                break;
            case BLUETOOTH_NOT_AVAILABLE:
                msg = "Bluetooth are off or your device is not pair to: " + headSetName;
                break;
            case DEVICE_NOT_FOUND:
                msg += "doesn't found, make sure that the distance in not longer then 10 meters";
                break;
            case DEVICE_NOT_CONNECTED:
                msg += "is not connected :(";
                break;
        }

        Utils.showToastMessage(getApplicationContext(), msg);
    }
    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_menu, menu);
//        return true;
//    }

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
