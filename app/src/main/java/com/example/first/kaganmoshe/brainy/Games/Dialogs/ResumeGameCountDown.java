package com.example.first.kaganmoshe.brainy.Games.Dialogs;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link ResumeGameCountDown.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link ResumeGameCountDown#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ResumeGameCountDown extends GameDialog {
    public static final int COUNTDOWN_TIME = 3;
    public static final String COUNTDOWN_TIME_TEXT = String.valueOf(COUNTDOWN_TIME);

    private int mCurrTime = COUNTDOWN_TIME;
    private Runnable mCounter;

    public interface ResumeGameCommunicator extends GameDialogCommunicator{
        void onGameResumed();
    }

    private TextView mCountdownView;

    public ResumeGameCountDown(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_game_countdown, container);
        mCountdownView = (TextView) view.findViewById(R.id.countdownTimer);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Handler handler = new Handler();
        mCounter = new Runnable(){

            public void run() {
                if (isShowing) {
                    if (mCurrTime <= 0) {
                        mCurrTime = COUNTDOWN_TIME;
                        finishResuming();
                    } else {
                        mCountdownView.setText(String.valueOf(mCurrTime--));
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
    }

    private void finishResuming() {
        if(this.isVisible()) {
            ((ResumeGameCommunicator) mListener).onGameResumed();
            dismiss();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }

    @Override
    public void onResume(){
        super.onResume();

        mCurrTime = COUNTDOWN_TIME;
        mCountdownView.setText(COUNTDOWN_TIME_TEXT);
        mCounter.run();
    }
}
