package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.first.kaganmoshe.brainy.CrazyCube.CCConfigActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HABConfigActivity;
import com.example.first.kaganmoshe.brainy.MindShooter.MindShooterConfigActivity;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.IHeadSetData;


public class GamesActivity extends ActionBarAppActivity implements IHeadSetData {

    public enum EGameRow {
        GUESS_THE_NUMBER(GUESS_THE_NUMBER_STR, GuessTheNumberConfigActivity.class, R.drawable.numbers),
        HOT_AIR_BALLOON(HOT_AIR_BALLOON_STR, HABConfigActivity.class, R.drawable.hot_air_balloon),
        CRAZY_CUBE(CRAZY_CUBE_STR, CCConfigActivity.class, R.drawable.kuku_cube),
        MIND_SHOOTER(MIND_SHOOTER_STR, MindShooterConfigActivity.class, R.drawable.ic_kavent);

        private String name;
        private Class targetActivity;
        private int imageId;

        EGameRow(String name, Class targetActivity, int imageId) {
            this.name = name;
            this.targetActivity = targetActivity;
            this.imageId = imageId;
        }

        public static EGameRow getGameRowByName(String name) {
            EGameRow gameRowResult = null;

            for (EGameRow gameRow : values()) {
                if (gameRow.name.equals(name)) {
                    gameRowResult = gameRow;
                    break;
                }
            }

            return gameRowResult;
        }

        public Class getTargetActivity() {
            return targetActivity;
        }

        @Override
        public String toString() {
            return name;
        }

        //
        public int getImageId() {
            return imageId;
        }
    }

    private static final String[] gamesTitles = new String[EGameRow.values().length];
    private static final Integer[] gamesImagesId = new Integer[EGameRow.values().length];

    //    private TextView toolbarText;
    private static final String MENU_TOOLBAR_TEXT = "Menu";
    public static final String GUESS_THE_NUMBER_STR = "Guess The Number";
    public static final String HOT_AIR_BALLOON_STR = "HotAir Balloon";
    public static final String CRAZY_CUBE_STR = "Crazy Cube";
    public static final String MIND_SHOOTER_STR = "Mind Shooter";
    private static MenuCustomList adapter;

    private ListView list;

//    private static final String[] titles = {
//            GUESS_THE_NUMBER_STR,
//            HOT_AIR_BALLOON_STR,
//            CRAZY_CUBE_STR,
//            MIND_SHOOTER_STR
//    };
//    private static final ArrayList<EGameRow> gamesRows = new ArrayList<>();
//    private static final Integer[] imageId = {
//            R.drawable.numbers,
//            R.drawable.hot_air_balloon,
//            R.drawable.kuku_cube,
//            R.drawable.ic_kavent
//    };

    private static final String[] reviews = {
            "Figure which number am I thinking of!",
            "Lift the hot air balloon with your mind!",
            "There is only one different cube, Can you find it?",
            "Shoot the target using your mind!"
    };

    private EConnectionState currnetState = EConnectionState.IDLE;

    static {
        int i = 0;

        for (EGameRow gameRow : EGameRow.values()) {
            gamesTitles[i] = gameRow.toString();
            gamesImagesId[i++] = gameRow.getImageId();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
//        this.setOnBackPressedActivity(ConnectionActivity.class);

        if (adapter == null) {
            adapter = new
                    MenuCustomList(GamesActivity.this, gamesTitles, gamesImagesId, reviews, R.layout.games_list_row);
        }

        list = (ListView) findViewById(R.id.gamesList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO - fix outside click events when popup is open
                Utils.startNewActivity((Activity) view.getRootView().getContext(),
                        EGameRow.getGameRowByName(gamesTitles[+position]).getTargetActivity());
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!homeButtonPopup.isShowing()) {
            Utils.startNewActivity(this, ConnectionActivity.class);
        } else {
            homeButtonPopup.dismiss();
        }
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
