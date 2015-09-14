package com.example.first.kaganmoshe.brainy.Games.Dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameHelpDialog extends GameDialog {

    private String mHelpTitleText;
    private String mHelpContentText;

    public interface GameHelpCommunicator extends GameDialog.GameDialogCommunicator {
        void onStartClicked();
    }

    public GameHelpDialog() {
    }

    public void setHelpContentText(String text){
        mHelpContentText = text;
    }

    public void setHelpTitleText(String text){
        mHelpTitleText = text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_help_dialog, container, false);

        ((TextView)view.findViewById(R.id.helpContentTextView)).setText(mHelpContentText);
        ((TextView)view.findViewById(R.id.helpTitleTextView)).setText(mHelpTitleText);

        view.findViewById(R.id.startGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartButtonClicked();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }

    private void onStartButtonClicked() {
        ((GameHelpCommunicator) mListener).onStartClicked();
    }
}
