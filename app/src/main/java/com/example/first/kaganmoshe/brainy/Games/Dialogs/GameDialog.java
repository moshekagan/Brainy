package com.example.first.kaganmoshe.brainy.Games.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Window;

/**
 * Created by tamirkash on 8/4/15.
 */
public abstract class GameDialog extends DialogFragment {

    protected GameDialogCommunicator mListener;
    protected boolean isShowing = false;

    public GameDialog() {
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        isShowing = true;
        mListener.onDialogShow(this.getClass());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface GameDialogCommunicator {
        void onDialogBackClicked(Class thisClass);

        void onDialogShow(Class thisClass);
    }

    public void setListener(GameDialogCommunicator gameScreen) {
        this.mListener = gameScreen;
    }

    @Override
    public void onResume() {
        super.onResume();

        isShowing = true;
    }

    protected void fireBackClickedEvent() {
        mListener.onDialogBackClicked(this.getClass());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().
                requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        isShowing = false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        isShowing = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         @Override
                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                                 fireBackClickedEvent();
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     }
        );

        isShowing = true;
    }

    @Override
    public void onStop() {
        super.onStop();

        isShowing = false;
    }
}
