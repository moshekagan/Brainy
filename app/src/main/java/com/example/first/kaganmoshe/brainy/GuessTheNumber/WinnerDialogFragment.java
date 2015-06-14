package com.example.first.kaganmoshe.brainy.GuessTheNumber;


import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
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
    private Button continueButton;
    private gameCommunicator gameScreen;

    public interface gameCommunicator {
        public void continueNextScreen();

        public void backKeyPressed();
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
        continueButton = (Button) rootView.findViewById(R.id.continueButton);
        title = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

        Utils.changeFont(getActivity().getAssets(), title);
        initButtons();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         @Override
                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                                 gameScreen.backKeyPressed();
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     }
        );
    }

    private void initButtons() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.continueNextScreen();
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        playWinnerSound();

        // request a window without the title
        dialog.getWindow().

                requestFeature(Window.FEATURE_NO_TITLE);

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