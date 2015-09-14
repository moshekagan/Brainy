package com.example.first.kaganmoshe.brainy.DailyPractice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.first.kaganmoshe.brainy.AppActivities.AppActivity;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.GameDialog;
import com.example.first.kaganmoshe.brainy.R;

public class DPStartDialog extends GameDialog {

    public interface DPStartCommunicator extends GameDialog.GameDialogCommunicator {
        void onStartClicked();

        void onSchedulePracticeClicked();
    }

    public DPStartDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dpstart_dialog, container);

        view.findViewById(R.id.startGameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartButtonClicked();
            }
        });

        view.findViewById(R.id.scheduleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScheduleClicked();
            }
        });

        view.findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        view.findViewById(R.id.scheduleButton).setOnTouchListener(AppActivity.TOUCH);

        return view;
    }

    private void onScheduleClicked() {
        ((DPStartCommunicator) mListener).onSchedulePracticeClicked();
    }

    private void onStartButtonClicked() {
        ((DPStartCommunicator) mListener).onStartClicked();
    }

    protected void onBackPressed() {
        dismiss();
    }
}
