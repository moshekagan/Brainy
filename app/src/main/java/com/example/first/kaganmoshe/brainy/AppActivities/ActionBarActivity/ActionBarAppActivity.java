package com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;

import com.example.first.kaganmoshe.brainy.AppActivities.AppActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MenuCustomList;
import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.AppManagement.SettingsFragment;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.IHeadSetData;

public abstract class ActionBarAppActivity extends AppActivity implements IHeadSetData,
        SettingsFragment.SettingsCommunicator {

    public final static String MAIN_ACTIVITY_STR = "Main";
    public final static String SETTINGS_STR = "Settings";
    public final static String QUIT_STR = "Quit";

    private final static String[] POPUP_MENU_TITLES = {MAIN_ACTIVITY_STR, SETTINGS_STR,
            QUIT_STR};

//    ArrayAdapter actionsList;
//    private View homeButtonView = null;

    private Integer[] imageId = {
            R.drawable.hot_air_balloon,
            R.drawable.settings_icon,
            R.drawable.quit_icon
    };
    private String[] reviews = {
            "",
            "",
            ""
    };

    protected ActionBar mActionBar;
    private MenuItem mConnectionMenuItem;
    protected ListPopupWindow mHomeButtonPopup;
    protected ViewGroup mMeasureParent;
    protected static final SettingsFragment mSettingsFragment = new SettingsFragment();
    protected android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
    private static final int POPUP_MENU_ROW_PADDING = 100;
    private static int popupMenuRowWidth = 0;

//    private Menu mMenu;

//    private boolean homeButtonDisabled = false;


    @Override
    public void onAttentionReceived(int attValue) {

    }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, final EConnectionState connectionState) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNewConnectionIconByConnectionState(connectionState);
            }
        });
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }

    @Override
    public void onSettingsShow() {

    }

//    @Override
//    public void onSettingsBackPressed() {
//
//    }

    @Override
    public void onSettingsDonePressed() {
        onResume();
    }

    @Override
    public void onSettingsBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getActionBar() != null && getActionBar().isShowing()) {
            AppManager.getInstance().getHeadSet().registerListener(this);

            if (mConnectionMenuItem != null) {
                setNewConnectionIconByConnectionState(AppManager.getInstance().getHeadSet().getConnctionState());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (getActionBar() != null && getActionBar().isShowing()) {
            AppManager.getInstance().getHeadSet().unregisterListener(this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if(savedInstanceState != null){
//            onCreateOptionsMenu(mMenu);
//        }

        mSettingsFragment.setCommunicator(this, getApplicationContext());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
        mHomeButtonPopup = new ListPopupWindow(this);
        MenuCustomList adapter = new
                MenuCustomList(this, POPUP_MENU_TITLES, imageId, reviews, R.layout.overflow_menu_popup_row);
        mHomeButtonPopup.setAdapter(adapter);
        mHomeButtonPopup.setAnchorView(findViewById(android.R.id.home));

        mHomeButtonPopup.setModal(true);

        if (popupMenuRowWidth == 0) {
            popupMenuRowWidth = measureContentWidth(adapter, getApplicationContext(), mMeasureParent);
        }
        mHomeButtonPopup.setContentWidth(popupMenuRowWidth);
        mHomeButtonPopup.setAnimationStyle(R.style.animation_menu_button_popup);

        //TODO - need to change if user picks settings or someplace that doest switch context
        mHomeButtonPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class cls = null;

                switch (POPUP_MENU_TITLES[+position]) {
                    case SETTINGS_STR:
                        onSettingsDialogClicked();
                        break;
                    case MAIN_ACTIVITY_STR:
                        if (parent.getContext().getClass() != MainActivity.class) {
                            Log.d("APP_CONTEXT", parent.getContext().getClass().toString());
                            onPopupMenuOptionSelected();
                        }
                        break;
                    case QUIT_STR:
                        onQuitClicked();
                        break;
                }

                if (mHomeButtonPopup.isShowing()) {
                    Log.d("HOME BUTTON", "setOnItemClickListener dismiss");
                    mHomeButtonPopup.dismiss();
                }

//                if (parent.getContext().getClass() != cls) {
//                    Log.d("APP_CONTEXT", parent.getContext().getClass().toString());
//                    onPopupMenuOptionSelected(cls);
//                }
            }
        });

//        mHomeButtonPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Log.d("HOME BUTTON", "setOnDismissListener homeButtonDisabled=false");
//                homeButtonDisabled = false;
//            }
//        });
    }

    protected void onQuitClicked() {
        finish();
    }

    protected void onSettingsDialogClicked() {
        mSettingsFragment.show(mFragmentManager, "Settings");
    }

    protected void onPopupMenuOptionSelected() {
        Utils.startNewActivity(this, MainActivity.class);
    }

    /**
     * This method will setup the top title bar (Action bar) content and display
     * values. It will also setup the custom background theme for ActionBar. You
     * can override this method to change the behavior of ActionBar for
     * particular Activity
     */
    protected void setupActionBar() {
        mActionBar = getActionBar();
        if (mActionBar == null)
            return;

        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(true);
//        mActionBar.setLogo(getResources().getDrawable(R.drawable.pause_icon5));
//        mActionBar.setBackgroundDrawable();
//        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
//        mActionBar.setDisplayShowHomeEnabled(false);


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_dropdown_item_1line, MENU);
//
//        BetterSpinner spinner = new BetterSpinner(mActionBar.getThemedContext());
//
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        spinner.setAdapter(adapter);
//        //TODO attach to an adapter of some sort
//        mActionBar.setCustomView(spinner);
    }

    private void setNewConnectionIconByConnectionState(EConnectionState connectionState) {

        //TODO - need to take care of the synchronization. the if statement is not enough!!!!
        if (mConnectionMenuItem != null) {
            switch (connectionState) {
                case DEVICE_CONNECTED:
                    mConnectionMenuItem.setIcon(R.drawable.good).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    break;
                case DEVICE_CONNECTING:
                    mConnectionMenuItem.setIcon(R.drawable.medium).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    break;
                case DEVICE_NOT_CONNECTED:
                case DEVICE_NOT_FOUND:
                case BLUETOOTH_NOT_AVAILABLE:
                case IDLE:
                    mConnectionMenuItem.setIcon(R.drawable.bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        mMenu = menu;

        if (mConnectionMenuItem == null) {
            mConnectionMenuItem = menu.add("Connection").setEnabled(false);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNewConnectionIconByConnectionState(AppManager.getInstance().getHeadSet().getConnctionState());
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !mHomeButtonPopup.isShowing()) {
            Log.d("HOME BUTTON", "onOptionsItemSelected calls homeMenuButtonClicked()");
            homeMenuButtonClicked();
        } else if (item.getItemId() != android.R.id.home && mHomeButtonPopup.isShowing()) {
            mHomeButtonPopup.dismiss();
            Log.d("HOME BUTTON", "onOptionsItemSelected dismiss");
        }

        return true;
    }

    protected void homeMenuButtonClicked() {
        Log.d("HOME BUTTON", "homeMenuButtonClicked()");
        //TODO - take care of things not happening twice!
//        actionsList = ArrayAdapter.createFromResource(this,
//                R.array.action_list, android.R.layout.simple_dropdown_item_1line);
//        popup.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, POPUP_MENU_TITLES));
        mHomeButtonPopup.show();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private static int measureContentWidth(ArrayAdapter adapter, Context context, ViewGroup measureParent) {
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();

        Log.d("POPUP", "Count=" + count);

        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (measureParent == null) {
                measureParent = new FrameLayout(context);
            }
            itemView = adapter.getView(i, itemView, measureParent);
            itemView.setLayoutParams(new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            Log.d("POPUP", "viewWidth=" + itemView.getMeasuredWidth() + " maxWidth=" + width);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        //TODO - const
        return width + POPUP_MENU_ROW_PADDING;
    }

//    @Override
//    public void onDestroy(){
//        mConnectionMenuItem = null;
//        super.onDestroy();
//    }
}
