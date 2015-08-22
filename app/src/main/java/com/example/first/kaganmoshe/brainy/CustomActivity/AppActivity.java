package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.first.kaganmoshe.brainy.AppManager;

/**
 * Created by tamirkash on 7/28/15.
 */
public abstract class AppActivity extends FragmentActivity implements View.OnClickListener {

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

    /**
     * Apply this Constant as touch listener for views to provide alpha touch
     * effect. The view must have a Non-Transparent background.
     */
    public static final TouchEffect TOUCH = new TouchEffect();

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
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(android.R.id.content));
        System.gc();
    }

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

    @Override
    protected void onPause(){
        super.onPause();
        AppManager.getInstance().pauseBackgroundMusic();
    }

    @Override
    protected void onResume(){
        super.onResume();
        AppManager.getInstance().playBackgroundMusic();
    }
}
