package com.example.first.kaganmoshe.brainy.Games.Dialogs;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.R;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishGameDialog extends GameDialog {

    //TODO - replace with soundPool
    private MediaPlayer winnerSound;
    private TextView mTitleTextView;
    private Button mContinueButton;
    private String mTitle = "Well Done";

    private String mDPTitle;
    private String mDPScore;
    private String mDPBestScore;
    private String mDPConcentration;

    private int layoutID = R.layout.winner_dialog;

    public FinishGameDialog() {
    }

    public interface FinishGameCommunicator extends GameDialogCommunicator {
        void onFinishGameContinueClicked();
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setScoreStat(int score) {
        this.mDPScore = String.valueOf(score);
    }

    public void setBestScoreStat(int bestScore) {
        this.mDPBestScore = String.valueOf(bestScore);
    }

    public void setConcentrationScoreStat(int concentration) {
        this.mDPConcentration = String.valueOf(concentration);
    }

    public void setDPTitle(String dpTitle) {
        this.mDPTitle = dpTitle;
    }

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
        mContinueButton = (Button) rootView.findViewById(R.id.continueButton);
        mTitleTextView = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

        mTitleTextView.setText(mTitle);
        initButtons();

        if (layoutID == R.layout.finish_game_dp_dialog) {
            addStats(rootView);
        }

        return rootView;
    }

    private void addStats(View rootView) {
        ((TextView) rootView.findViewById(R.id.winnerDialogDPTitle)).setText(mDPTitle);
        ((TextView) rootView.findViewById(R.id.scoreStatTextView)).setText(mDPScore);
        ((TextView) rootView.findViewById(R.id.bestScoreStatTextView)).setText(mDPBestScore);
        ((TextView) rootView.findViewById(R.id.concentrationStatTextView)).setText(mDPConcentration);
    }

    private void initButtons() {
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowing = false;
                ((FinishGameCommunicator) mListener).onFinishGameContinueClicked();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        winnerSound = MediaPlayer.create(getActivity(), R.raw.finish_sound);
        playSound();

        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        winnerSound.stop();
    }

    private void playSound() {
        winnerSound.start();
    }
}