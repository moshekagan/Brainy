package com.example.first.kaganmoshe.brainy.HotAirBallon;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.example.first.kaganmoshe.brainy.Utils;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.WinnerDialogFragment;

import com.example.first.kaganmoshe.brainy.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class StartGameDialogFragment extends DialogFragment {

    private MediaPlayer winnerSound;
    private TextView title;
    private Button continueButton;
    private gameCommunicator gameScreen;
    private String titleText = "Great Work!";
    private String continueButtonText = "Continue";

    public interface gameCommunicator {
        public void startGameScreen();
        public void backKeyPressed();
    }

    public void setGameScreen(gameCommunicator gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setTitleText(String titleText) { this.titleText = titleText; }

    public void setContinueButtonText(String continueButtonText) { this.continueButtonText = continueButtonText; }

    public StartGameDialogFragment() {
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
        int style=DialogFragment.STYLE_NO_TITLE;
        int theme=android.R.style.Theme_Translucent;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_strat_game_dialog_fragmet, container,
                false);
        getDialog().setCanceledOnTouchOutside(false);
        continueButton = (Button) rootView.findViewById(R.id.continueButton);
        title = (TextView) rootView.findViewById(R.id.winnerDialogTitle);

//        continueButton.setText(continueButtonText);
//        title.setText(titleText);

//        Utils.changeFont(getActivity().getAssets(), title);
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
                gameScreen.startGameScreen();
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
