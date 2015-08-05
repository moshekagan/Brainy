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

import com.example.first.kaganmoshe.brainy.LoginActivity;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.MenuCustomList;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.SettingsActivity;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * Created by tamirkash on 7/28/15.
 */
public class AppActivity extends FragmentActivity implements View.OnClickListener {

    //    String[] actionsStrings = getResources().getStringArray(R.array.action_list);
    //TODO - make the titles generic
    private final static String[] POPUP_MENU_TITLES = {"Games", "Settings", "Quit"};
    protected ActionBar actionBar;
    ArrayAdapter actionsList;

    private Integer[] imageId = {
            R.drawable.hot_air_balloon,
            R.drawable.hot_air_balloon,
            R.drawable.hot_air_balloon
    };
    private String[] reviews = {
            "bla",
            "bla",
            "bla"
    };

    protected ListPopupWindow homeButtonPopup;
    protected ViewGroup mMeasureParent;

    private static final int POPUP_MENU_ROW_PADDING = 50;
    private static int popupMenuRowWidth = 0;

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

//    public void setOnBackPressedActivityTarget(Class onBackPressedActivity) {
//        this.onBackPressedActivityTarget = onBackPressedActivity;
//    }

//    @Override
//    public void onBackPressed() {
//        if (onBackPressedActivityTarget == null)
//            super.onBackPressed();
//        else
//            Utils.startNewActivity(this, onBackPressedActivityTarget);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle extras = getIntent().getExtras();
//        String classname=extras.getString("class");
//        Class<?> clazz = Class.forName(classname);
//        Intent i = new Intent(Setting.this, clazz);
//        startActivity(i);
//        }
//        onBackPressedActivityTarget = getIntent().getStringExtra("CLASS_NAME");
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

                homeButtonPopup.dismiss();

                switch (POPUP_MENU_TITLES[+position]) {
                    case "Settings":
                        cls = SettingsActivity.class;
                        break;
                    case "Games":
                        cls = MenuActivity.class;
                        break;
                    case "Quit":
                        cls = LoginActivity.class;
                        break;
                }

                if(parent.getContext().getClass() != cls) {
                    Log.d("APP_CONTEXT", parent.getContext().getClass().toString());
                    onPopupMenuOptionSelected(cls);
                }
            }
        });
    }

    protected void onPopupMenuOptionSelected(Class cls) {
        Utils.startNewActivity(this, cls);
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
        menu.add("Connection").setIcon(R.drawable.bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !homeButtonPopup.isShowing()) {
            showPopup();
        } else if (homeButtonPopup.isShowing()) {
            homeButtonPopup.dismiss();
        }

        return true;
    }

    private void showPopup() {
        //TODO - take care of things not happening twice!
//        actionsList = ArrayAdapter.createFromResource(this,
//                R.array.action_list, android.R.layout.simple_dropdown_item_1line);
//        popup.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, POPUP_MENU_TITLES));

        homeButtonPopup.show();
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
}
