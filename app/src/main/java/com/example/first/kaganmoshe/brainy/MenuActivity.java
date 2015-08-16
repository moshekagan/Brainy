package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.first.kaganmoshe.brainy.CrazyCube.CCConfigActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HABConfigActivity;
import com.example.first.kaganmoshe.brainy.MindShooter.MindShooterConfigActivity;

import EEG.EConnectionState;
import EEG.ESignalVolume;
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

                Utils.startNewActivity((Activity)view.getRootView().getContext(), cls);
            }
        });
    }

    @Override
    public void onBackPressed(){
        Utils.startNewActivity(this, SettingsActivity.class);
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        super.onHeadSetChangedState(headSetName, connectionState);
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
