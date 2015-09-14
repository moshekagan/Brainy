package com.example.first.kaganmoshe.brainy.AppManagement;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.first.kaganmoshe.brainy.R;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SettingsFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SettingsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SettingsFragment extends DialogFragment {

    private Context mContext;
    private SettingsCommunicator mCurrScreen;
    private boolean mIsShowing = false;

    public boolean isShowing() {
        return mIsShowing;
    }

    public interface SettingsCommunicator {
        void onSettingsShow();

        void onSettingsBackPressed();

        void onSettingsDonePressed();
    }

    public void setCommunicator(SettingsCommunicator currScreen, Context context) {
        this.mContext = context;
        this.mCurrScreen = currScreen;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        mIsShowing = true;
        super.show(manager, tag);
        mCurrScreen.onSettingsShow();
    }

    public SettingsFragment() {
    }

    @Override
    public void onResume(){
        super.onResume();

        mIsShowing = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);

        final AudioManager audioManager = AppManager.getInstance().getAudioManager();
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        view.findViewById(R.id.decreaseButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // decrease the volume and show the ui
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });

        view.findViewById(R.id.increaseButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // increase the volume and show the ui
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
                        AudioManager.FLAG_SHOW_UI);
            }
        });

        view.findViewById(R.id.muteButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // mute the volume and show the ui
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                        AudioManager.FLAG_SHOW_UI);
            }
        });

        view.findViewById(R.id.doneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShowing = false;
                mCurrScreen.onSettingsDonePressed();
                dismiss();
            }
        });

        view.findViewById(R.id.musicDisableButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().muteMusicForUserRequest(true);
            }
        });

        view.findViewById(R.id.musicEnableButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().muteMusicForUserRequest(false);
            }
        });

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().
                requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart(){
        super.onStart();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         @Override
                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                                 mIsShowing = false;
                                                 mCurrScreen.onSettingsBackPressed();
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     });
    }
}
