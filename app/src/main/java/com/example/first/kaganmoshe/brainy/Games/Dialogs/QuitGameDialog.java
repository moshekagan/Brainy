package com.example.first.kaganmoshe.brainy.Games.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * Created by tamirkash on 8/4/15.
 */
public class QuitGameDialog extends GameDialog {

    private Button mContinueButton;
    private Button mLeaveButton;
    private TextView mTitleTextView;

    public interface QuitGameCommunicator extends GameDialogCommunicator{
        void onQuitGameConfirmed();
        void onQuitGameCanceled();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.game_quit_dialog, container,
                false);
        mContinueButton = (Button) rootView.findViewById(R.id.continueButton);
        mTitleTextView = (TextView) rootView.findViewById(R.id.quitGameDialogTitle);
        mLeaveButton = (Button) rootView.findViewById(R.id.quitButton);

        initButtons();

        return rootView;
    }

    private void initButtons() {
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowing = false;
                dismiss();
                ((QuitGameCommunicator) mListener).onQuitGameCanceled();
            }
        });

        mLeaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuitGameCommunicator) mListener).onQuitGameConfirmed();
            }
        });
    }
}
