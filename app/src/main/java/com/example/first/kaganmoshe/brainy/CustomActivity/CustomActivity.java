package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.SettingsActivity;
import com.example.first.kaganmoshe.brainy.Utils;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import EEG.EHeadSetType;

/**
 * Created by tamirkash on 7/28/15.
 */
public class CustomActivity extends FragmentActivity implements View.OnClickListener {

//    String[] actionsStrings = getResources().getStringArray(R.array.action_list);
    private final static String[] STRINGS = {"Games", "Settings", "Quit"};
    protected ActionBar actionBar;
    protected Class onBackPressedActivity = null;
    ArrayAdapter actionsList;

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

    public void setOnBackPressedActivity(Class onBackPressedActivity) {
        this.onBackPressedActivity = onBackPressedActivity;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedActivity == null)
            super.onBackPressed();
        else
            Utils.startNewActivity(this, onBackPressedActivity);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_overflow_button, menu);
//        return true;

//        MenuInflater inflater = getSupportMenuInflater();
//        inflater.inflate(R.menu.activity_main, menu);
        menu.add("Connection").setIcon(R.drawable.bad)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
//                        doStuff();
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showPopup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPopup() {
        ListPopupWindow popup = new ListPopupWindow(this);
        actionsList = ArrayAdapter.createFromResource(this,
                R.array.action_list, android.R.layout.simple_dropdown_item_1line);
//        popup.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, STRINGS));
        popup.setAdapter(actionsList);
        popup.setAnchorView(findViewById(android.R.id.home));
//        popup.setWidth(ListPopupWindow.WRAP_CONTENT);
        popup.setContentWidth(400);
        popup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "Clicked item " + position, Toast.LENGTH_SHORT).show();
            }
        });
        popup.show();
    }
}
