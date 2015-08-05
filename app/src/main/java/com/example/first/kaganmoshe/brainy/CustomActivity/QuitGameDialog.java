package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * Created by tamirkash on 8/4/15.
 */
public class QuitGameDialog extends GameDialog {

    private Button continueButton;
    private Button leaveButton;
    private TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_quit_dialog, container,
                false);
        continueButton = (Button) rootView.findViewById(R.id.continueButton);
        title = (TextView) rootView.findViewById(R.id.quitGameDialogTitle);
        leaveButton = (Button) rootView.findViewById(R.id.quitButton);

        Utils.changeFont(getActivity().getAssets(), title);
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
//                                                 gameScreen.onBackPressed();
//                                                 return true;
//                                             } else {
//                                                 return false;
//                                             }
//                                         }
//                                     }
//        );
//    }

    private void initButtons() {
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameScreen.onPopupDialogCanceled();
                dismiss();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameScreen.onPopupDialogLeaveClicked();
            }
        });
    }

    public void show(FragmentManager manager, String tag, Class targetActivity){

    }
}
