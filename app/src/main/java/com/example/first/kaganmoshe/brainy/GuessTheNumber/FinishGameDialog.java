package com.example.first.kaganmoshe.brainy.GuessTheNumber;


import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameDialog;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishGameDialog extends GameDialog {

    private MediaPlayer winnerSound;
    private TextView title;
    private Button continueButton;

    public FinishGameDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        winnerSound.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.winner_dialog, container,
                false);
        getDialog().setCanceledOnTouchOutside(false);
        continueButton = (Button) rootView.findViewById(R.id.continueButton);
        title = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

        Utils.changeFont(getActivity().getAssets(), title);
        initButtons();

        return rootView;
    }

    private void initButtons() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.onFinishDialogConfirmed();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        playWinnerSound();

        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        winnerSound.stop();
    }

    private void playWinnerSound() {
        winnerSound = MediaPlayer.create(getActivity(), R.raw.winner_sound);
        winnerSound.start();
    }
}