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

//    @Override
//    protected void fireBackClickedEvent() {
//        mListener.onDialogBackClicked(QuitGameDialog.class);
//    }

//    @Override
//    protected void fireBackClickedEvent() {
//        isShowing = false;
//        super.fireBackClickedEvent();
//    }

    public interface QuitGameCommunicator extends GameDialogCommunicator{
        void onQuitGameConfirmed();
        void onQuitGameCanceled();
    }

//    @Override
//    public void setListener(GameDialogCommunicator mListener){
//        this.mListener = mListener;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.game_quit_dialog, container,
                false);
        mContinueButton = (Button) rootView.findViewById(R.id.continueButton);
        mTitleTextView = (TextView) rootView.findViewById(R.id.quitGameDialogTitle);
        mLeaveButton = (Button) rootView.findViewById(R.id.quitButton);

//        Utils.changeFont(getActivity().getAssets(), mTitleTextView);
        initButtons();

        return rootView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//                                         @Override
//                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//                                                 mListener.onGameDialogBackPressed();
//                                                 return true;
//                                             } else {
//                                                 return false;
//                                             }
//                                         }
//                                     }
//        );
//    }

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

//    public void show(FragmentManager manager, String tag, Class targetActivity){
//
//    }
}
