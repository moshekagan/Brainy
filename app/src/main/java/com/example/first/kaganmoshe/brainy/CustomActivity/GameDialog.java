package com.example.first.kaganmoshe.brainy.CustomActivity;

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

    protected GameDialogCommunicator gameScreen;

    public GameDialog(){}

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
        gameScreen.onDialogShow();
    }

    public interface GameDialogCommunicator{
        void onPopupDialogCanceled();
        void onPopupDialogLeaveClicked();
        void onFinishDialogConfirmed();
        void onBackPressed();
        void onDialogShow();
    }

    public void setGameScreen(GameDialogCommunicator gameScreen){
        this.gameScreen = gameScreen;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         @Override
                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                                 gameScreen.onBackPressed();
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     }
        );
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().
                requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
