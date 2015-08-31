package com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.IHeadSetData;

/**
 * Created by tamirkash on 8/12/15.
 */
public class ActionBarConnectionItem implements IHeadSetData {
    private static MenuItem connectionMenuItem;
    private static Drawable good;
    private static Drawable bad;
    private static Drawable medium;
    private static Activity activity;

    public ActionBarConnectionItem(Activity activity, MenuItem connectionMenuItem, Drawable good, Drawable bad, Drawable medium) {
        this.good = good;
        this.medium = medium;
        this.bad = bad;
        this.connectionMenuItem = connectionMenuItem;
        this.activity = activity;

        AppManager.getInstance().getHeadSet().registerListener(this);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNewConnectionIconByConnectionState(AppManager.getInstance().getHeadSet().getConnctionState());
            }
        });

//        connectionMenuItem.setIcon(R.drawable.bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        connectionMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setNewConnectionIconByConnectionState(EConnectionState.DEVICE_CONNECTED);
                return false;
            }
        });
    }

    private void setNewConnectionIconByConnectionState(EConnectionState connectionState){
        switch (connectionState) {
            case DEVICE_CONNECTED:
                connectionMenuItem.setIcon(good).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
            case DEVICE_CONNECTING:
                connectionMenuItem.setIcon(medium).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
            case DEVICE_NOT_CONNECTED:
            case DEVICE_NOT_FOUND:
            case BLUETOOTH_NOT_AVAILABLE:
            case IDLE:
                connectionMenuItem.setIcon(bad).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                break;
        }
    }

    public static MenuItem getConnectionMenuItem() {
        return connectionMenuItem;
    }

    public static Drawable getBad() {
        return bad;
    }

    public static Drawable getGood() {
        return good;
    }

    public static Drawable getMedium() {
        return medium;
    }

    @Override
    public void onAttentionReceived(int attValue) {

    }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, final EConnectionState connectionState) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNewConnectionIconByConnectionState(connectionState);
            }
        });
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }
    //TODO - DON'T FORGET TO USE THIS
    public void unregisterConnectionMenuItem(){
        AppManager.getInstance().getHeadSet().unregisterListener(this);
    }
}
