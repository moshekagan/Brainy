package com.example.first.kaganmoshe.brainy.AppActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.Games.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.DailyPractice.DPStartDialog;
import com.example.first.kaganmoshe.brainy.HistoryDataBase.HistoryActivity;
import com.example.first.kaganmoshe.brainy.Games.GuessTheNumber.GuessTheNumberGameActivity;
import com.example.first.kaganmoshe.brainy.Games.HotAirBallon.HotAirBalloonGameActivity;
import com.example.first.kaganmoshe.brainy.Games.MindShooter.MindShooterGameActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import EEG.EConnectionState;
import EEG.ESignalVolume;


public class MainActivity extends ActionBarAppActivity implements DPStartDialog.DPStartCommunicator {

    public enum EGameItem {
        //        GUESS_THE_NUMBER(GUESS_THE_NUMBER_STR, GuessTheNumberConfigActivity.class, R.drawable.numbers),
//        HOT_AIR_BALLOON(HOT_AIR_BALLOON_STR, HABConfigActivity.class, R.drawable.hot_air_balloon),
//        CRAZY_CUBE(CRAZY_CUBE_STR, CCConfigActivity.class, R.drawable.kuku_cube),
//        MIND_SHOOTER(MIND_SHOOTER_STR, MindShooterConfigActivity.class, R.drawable.ic_kavent);
        GUESS_THE_NUMBER(GUESS_THE_NUMBER_STR, GuessTheNumberGameActivity.class, R.drawable.numbers),
        HOT_AIR_BALLOON(HOT_AIR_BALLOON_STR, HotAirBalloonGameActivity.class, R.drawable.hot_air_balloon),
        CRAZY_CUBE(CRAZY_CUBE_STR, CrazyCubeActivity.class, R.drawable.kuku_cube),
        MIND_SHOOTER(MIND_SHOOTER_STR, MindShooterGameActivity.class, R.drawable.ic_kavent);

        private String name;
        private Class targetActivity;
        private int imageId;

        EGameItem(String name, Class targetActivity, int imageId) {
            this.name = name;
            this.targetActivity = targetActivity;
            this.imageId = imageId;
        }

        public static EGameItem getGameRowByName(String name) {
            EGameItem gameRowResult = null;

            for (EGameItem gameRow : values()) {
                if (gameRow.name.equals(name)) {
                    gameRowResult = gameRow;
                    break;
                }
            }

            return gameRowResult;
        }

        public static String getGameNameByClass(Class gameClass){
            String gameName = null;

            for (EGameItem gameRow : values()) {
                if (gameRow.targetActivity.equals(gameClass)) {
                    gameName = gameRow.toString();
                    break;
                }
            }

            return gameName;
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

    //    private TextView toolbarText;
    private static MenuCustomList mGamesRowsAdapter;

    private final DPStartDialog mDailyPracticeStartDialog = new DPStartDialog();
    private ListView mGamesRowsListView;
    private static final String MENU_TOOLBAR_TEXT = "Menu";

    public static final String GUESS_THE_NUMBER_STR = "Guess The Number";
    public static final String HOT_AIR_BALLOON_STR = "HotAir Balloon";
    public static final String CRAZY_CUBE_STR = "Crazy Cube";
    public static final String MIND_SHOOTER_STR = "Mind Shooter";
    private static final String[] mGamesTitles = new String[EGameItem.values().length];
    private static final Integer[] mGamesImagesId = new Integer[EGameItem.values().length];
    private static final String[] mGamesReviews = {
            "Figure which number am I thinking of!",
            "Lift the hot air balloon with your mind!",
            "There is only one different cube, Can you find it?",
            "Shoot the target using your mind!"
    };

    private EConnectionState mCurrnetConnectionState = EConnectionState.IDLE;

    static {
        int i = 0;

        for (EGameItem gameRow : EGameItem.values()) {
            mGamesTitles[i] = gameRow.toString();
            mGamesImagesId[i++] = gameRow.getImageId();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
//        this.setOnBackPressedActivity(ConnectionActivity.class);

        if (mGamesRowsAdapter == null) {
            mGamesRowsAdapter = new
                    MenuCustomList(MainActivity.this, mGamesTitles, mGamesImagesId, mGamesReviews, R.layout.games_list_row);
        }

        mGamesRowsListView = (ListView) findViewById(R.id.gamesList);
        mGamesRowsListView.setAdapter(mGamesRowsAdapter);
        mGamesRowsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO - fix outside click events when popup is open
                Utils.startNewActivity((Activity) view.getRootView().getContext(),
                        EGameItem.getGameRowByName(mGamesTitles[+position]).getTargetActivity());
            }
        });

        setTouchNClick(R.id.dailyPracticeButton);
        setTouchNClick(R.id.historyButton);

        findViewById(R.id.historyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryClicked();
            }
        });

        findViewById(R.id.dailyPracticeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDailyPracticeClicked();
            }
        });

        mDailyPracticeStartDialog.setListener(this);
    }

    private void onDailyPracticeClicked() {
        mDailyPracticeStartDialog.show(mFragmentManager, "DPStartDialog");
    }

    private void onHistoryClicked() {
        Utils.startNewActivity(this, HistoryActivity.class);
    }

    @Override
    public void onBackPressed() {
        if (!mHomeButtonPopup.isShowing()) {
            Utils.startNewActivity(this, ConnectionActivity.class);
        } else {
            mHomeButtonPopup.dismiss();
        }
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        super.onHeadSetChangedState(headSetName, connectionState);
        mCurrnetConnectionState = connectionState;
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


    @Override
    public void onStartClicked() {
        mDailyPracticeStartDialog.dismiss();
        AppManager.getGamesManager().startDailyPractice(this);
    }

    @Override
    public void onDialogBackClicked(Class thisClass) {
        mDailyPracticeStartDialog.dismiss();
    }

    @Override
    public void onDialogShow(Class thisClass) {

    }
}
