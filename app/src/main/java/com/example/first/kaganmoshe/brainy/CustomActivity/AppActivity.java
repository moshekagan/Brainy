package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;

import com.example.first.kaganmoshe.brainy.AppManager;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.MenuCustomList;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.IHeadSetData;

/**
 * Created by tamirkash on 7/28/15.
 */
public class AppActivity extends FragmentActivity implements View.OnClickListener, IHeadSetData,
        SettingsFragment.SettingsCommunicator {

    //    String[] actionsStrings = getResources().getStringArray(R.array.action_list);
    //TODO - make the titles generic
    private final static String[] POPUP_MENU_TITLES = {"Games", "Settings", "Quit"};
    protected ActionBar actionBar;
    ArrayAdapter actionsList;
    private View homeButtonView = null;

    private Integer[] imageId = {
            R.drawable.hot_air_balloon,
            R.drawable.settings_icon,
            R.drawable.quit_icon
    };
    private String[] reviews = {
            "bla",
            "bla",
            "bla"
    };

    private MenuItem connectionMenuItem;
    protected ListPopupWindow homeButtonPopup;
    protected ViewGroup mMeasureParent;
    protected static final SettingsFragment settingsFragment = new SettingsFragment();
    protected android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private static final int POPUP_MENU_ROW_PADDING = 50;
    private static int popupMenuRowWidth = 0;
//    private static MenuItem connectionMenuIcon;
//    private static ActionBarConnectionItem actionBarConnectionItem;

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

    public static class TouchEffect implements View.OnTouchListener {

        /* (non-Javadoc)
         * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Drawable d = v.getBackground();
                d.mutate();
                d.setAlpha(150);
                v.setBackgroundDrawable(d);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                Drawable d = v.getBackground();
                d.setAlpha(255);
                v.setBackgroundDrawable(d);
            }
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getActionBar() != null && getActionBar().isShowing()) {
            AppManager.getInstance().getHeadSet().registerListener(this);

            if (connectionMenuItem != null) {
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

        settingsFragment.setCommunicator(this, getApplicationContext());
    }

    /**
     * Apply this Constant as touch listener for views to provide alpha touch
     * effect. The view must have a Non-Transparent background.
     */
    public static final TouchEffect TOUCH = new TouchEffect();

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
        homeButtonPopup = new ListPopupWindow(this);
        MenuCustomList adapter = new
                MenuCustomList(this, POPUP_MENU_TITLES, imageId, reviews, R.layout.overflow_menu_popup_row);
        homeButtonPopup.setAdapter(adapter);
        homeButtonPopup.setAnchorView(findViewById(android.R.id.home));

        if (popupMenuRowWidth == 0) {
            popupMenuRowWidth = measureContentWidth(adapter, getApplicationContext(), mMeasureParent);
        }
        homeButtonPopup.setContentWidth(popupMenuRowWidth);
        homeButtonPopup.setAnimationStyle(R.style.animation_menu_button_popup);

        //TODO - need to change if user picks settings or someplace that doest switch context
        homeButtonPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class cls = null;

                switch (POPUP_MENU_TITLES[+position]) {
                    case "Settings":
                        onSettingsDialogClicked();
                        break;
                    case "Games":
                        if (parent.getContext().getClass() != MenuActivity.class) {
                            Log.d("APP_CONTEXT", parent.getContext().getClass().toString());
                            onPopupMenuOptionSelected();
                        }
                        break;
                    case "Quit":
                        onQuitClicked();
                        break;
                }

                if (homeButtonPopup.isShowing()) {
                    homeButtonPopup.dismiss();
                }
//                if (parent.getContext().getClass() != cls) {
//                    Log.d("APP_CONTEXT", parent.getContext().getClass().toString());
//                    onPopupMenuOptionSelected(cls);
//                }
            }
        });
    }

    protected void onQuitClicked() {
        finish();
    }

    protected void onSettingsDialogClicked() {
        settingsFragment.show(fm, "Settings");
    }

    protected void onPopupMenuOptionSelected() {
        Utils.startNewActivity(this, MenuActivity.class);
    }

    /**
     * This method will setup the top title bar (Action bar) content and display
     * values. It will also setup the custom background theme for ActionBar. You
     * can override this method to change the behavior of ActionBar for
     * particular Activity
     */
    protected void setupActionBar() {
        actionBar = getActionBar();
        if (actionBar == null)
            return;

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setLogo(getResources().getDrawable(R.drawable.pause_icon5));
//        actionBar.setBackgroundDrawable();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(false);


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_dropdown_item_1line, MENU);
//
//        BetterSpinner spinner = new BetterSpinner(actionBar.getThemedContext());
//
//        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        spinner.setAdapter(adapter);
//        //TODO attach to an adapter of some sort
//        actionBar.setCustomView(spinner);
    }

    /**
     * Sets the touch and click listener for a view with given id.
     *
     * @param id the id
     * @return the view on which listeners applied
     */
    public View setTouchNClick(int id) {

        View v = setClick(id);
        if (v != null)
            v.setOnTouchListener(TOUCH);
        return v;
    }

    /**
     * Sets the click listener for a view with given id.
     *
     * @param id the id
     * @return the view on which listener is applied
     */
    public View setClick(int id) {

        View v = findViewById(id);
        if (v != null)
            v.setOnClickListener(this);
        return v;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

    }

    private void setNewConnectionIconByConnectionState(EConnectionState connectionState) {
        switch (connectionState) {
            case DEVICE_CONNECTED:
                connectionMenuItem.setIcon(R.drawable.good).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
            case DEVICE_CONNECTING:
                connectionMenuItem.setIcon(R.drawable.medium).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
            case DEVICE_NOT_CONNECTED:
            case DEVICE_NOT_FOUND:
            case BLUETOOTH_NOT_AVAILABLE:
            case IDLE:
                connectionMenuItem.setIcon(R.drawable.bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (connectionMenuItem == null) {
            connectionMenuItem = menu.add("Connection").setEnabled(false);
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
        if (item.getItemId() == android.R.id.home && !homeButtonPopup.isShowing()) {
            homeMenuButtonClicked();
        } else if (homeButtonPopup.isShowing()) {
            homeButtonPopup.dismiss();
        }

        return true;
    }

    protected void homeMenuButtonClicked() {
        //TODO - take care of things not happening twice!
//        actionsList = ArrayAdapter.createFromResource(this,
//                R.array.action_list, android.R.layout.simple_dropdown_item_1line);
//        popup.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, POPUP_MENU_TITLES));
        homeButtonPopup.show();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private static int measureContentWidth(ArrayAdapter adapter, Context context, ViewGroup mMeasureParent) {
        int width = 0;
        View itemView = null;
        int itemType = 0;
        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int count = adapter.getCount();

        for (int i = 0; i < count; i++) {
            final int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(context);
            }
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            width = Math.max(width, itemView.getMeasuredWidth());
        }
        //TODO - const
        return width + POPUP_MENU_ROW_PADDING;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(android.R.id.content));
        System.gc();
    }

//    @Override
//    public void onStop(){
//        super.onStop();
//
//
//    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            try {
                ((ViewGroup) view).removeAllViews();
            } catch (UnsupportedOperationException ignore) {
                //if can't remove all view (e.g. adapter view) - no problem
            }
        }
    }
}
