package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
    private static final int COUNTDOWN_TIME = 3;
    private static final String COUNTDOWN_TIME_TEXT = String.valueOf(COUNTDOWN_TIME);
    private int currTime = COUNTDOWN_TIME;

    public interface ResumeGameCommunicator extends GameDialogCommunicator{
        void onGameResumed();
    }

    private TextView mCountdownView;

    public ResumeGameCountDown(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume_game_countdown, container);
        mCountdownView = (TextView) view.findViewById(R.id.countdownTimer);
        mCountdownView.setText(COUNTDOWN_TIME_TEXT);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
                if(currTime <= 0) {
                    isShowing = false;
                    currTime = COUNTDOWN_TIME;
                    ((ResumeGameCommunicator)gameScreen).onGameResumed();
                    dismiss();
                    return;
                } else {
                    mCountdownView.setText(String.valueOf(currTime--));
                    handler.postDelayed(this, 1000);
                }
            }
        };

        counter.run();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }
}
