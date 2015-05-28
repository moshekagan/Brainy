package com.example.first.kaganmoshe.brainy.GuessTheNumber;


import android.app.Dialog;
import android.media.MediaPlayer;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WinnerDialogFragment extends DialogFragment {

    private MediaPlayer winnerSound;
    private TextView title;
    private Button playAgainButton;
    private Button backButton;
    private Button feedbackButton;
    private gameCommunicator gameScreen;

    public interface gameCommunicator{
        public void playAgain();
        public void feedback();
        public void back();
    }

    public void setGameScreen(gameCommunicator gameScreen) {
        this.gameScreen = gameScreen;
    }

    public WinnerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        winnerSound.stop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.winner_dialog, container,
                false);
        getDialog().setCanceledOnTouchOutside(false);
        backButton = (Button) rootView.findViewById(R.id.backButton);
        playAgainButton = (Button) rootView.findViewById(R.id.playAgainButton);
        feedbackButton = (Button) rootView.findViewById(R.id.feedbackButton);
        title = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

        Utils.changeFont(getActivity().getAssets(), title);
        initButtons();

        return rootView;
    }

    private void initButtons() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.back();
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.playAgain();
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.feedback();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        playWinnerSound();

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStop() {
        super.onStop();
        winnerSound.stop();
    }

    private void playWinnerSound(){
        winnerSound = MediaPlayer.create(getActivity(), R.raw.winner_sound);
        winnerSound.start();
    }
}
