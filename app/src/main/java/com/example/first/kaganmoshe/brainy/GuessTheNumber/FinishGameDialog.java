package com.example.first.kaganmoshe.brainy.GuessTheNumber;


import android.app.Dialog;
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

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishGameDialog extends GameDialog {

    private MediaPlayer winnerSound;
    private TextView titleTextView;
    private Button continueButton;
    private String title = "Well Done";
    private String dpTitle;

    private int layoutID = R.layout.winner_dialog;

    public FinishGameDialog() {
        // Required empty public constructor
    }

    public interface FinishGameCommunicator extends GameDialogCommunicator{
        void onFinishGameContinueClicked();
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setStats(HashMap<String, String> stats){

    }

    public void setDPTitle(String dpTitle){
        this.dpTitle = dpTitle;
    }

    //    @Override
//    protected void fireBackClickedEvent() {
//        gameScreen.onDialogBackClicked(FinishGameDialog.class);
//    }


    @Override
    public void onDetach() {
        super.onDetach();
        winnerSound.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layoutID, container,
                false);
        getDialog().setCanceledOnTouchOutside(false);
        continueButton = (Button) rootView.findViewById(R.id.continueButton);
        titleTextView = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

        titleTextView.setText(title);
        initButtons();

        if(layoutID == R.layout.finish_game_dp_dialog){
            ((TextView)rootView.findViewById(R.id.winnerDialogDPTitle)).setText(dpTitle);
            addStats();
        }

        return rootView;
    }

    private void addStats() {

    }

    private void initButtons() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowing = false;
                ((FinishGameCommunicator)gameScreen).onFinishGameContinueClicked();
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