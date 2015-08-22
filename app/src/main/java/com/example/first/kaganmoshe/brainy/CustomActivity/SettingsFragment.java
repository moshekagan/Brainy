package com.example.first.kaganmoshe.brainy.CustomActivity;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.AppManager;
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

    private Context context;
    private SettingsCommunicator currScreen;
    private AudioManager audioManager;
    private Button increaseButton;
    private Button decreaseButton;
    private Button muteButton;
    private Button doneButton;
    private Button musicEnableButton;
    private Button musicDisableButton;
    private boolean isShowing = false;

    public boolean isShowing() {
        return isShowing;
    }

    public interface SettingsCommunicator {
        void onSettingsShow();

        void onSettingsBackPressed();

        void onSettingsDonePressed();
    }

    public void setCommunicator(SettingsCommunicator currScreen, Context context) {
        this.context = context;
        this.currScreen = currScreen;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        isShowing = true;
        super.show(manager, tag);
        currScreen.onSettingsShow();
    }

    public SettingsFragment() {
    }

    @Override
    public void onResume(){
        super.onResume();

        isShowing = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        decreaseButton = (Button) view.findViewById(R.id.decreaseButton);
        decreaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // decrease the volume and show the ui
//                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1,
                        AudioManager.FLAG_SHOW_UI);
//                Toast.makeText(context.getApplicationContext(), "Volume decreased",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        increaseButton = (Button) view.findViewById(R.id.increaseButton);
        increaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // increase the volume and show the ui
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1,
                        AudioManager.FLAG_SHOW_UI);
//                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//                Toast.makeText(context.getApplicationContext(), "Volume increased",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        muteButton = (Button) view.findViewById(R.id.muteButton);
        muteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // mute the volume and show the ui
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0,
                        AudioManager.FLAG_SHOW_UI);
//                audioManager.setRingerMode(AudioManager.);
//                Toast.makeText(context.getApplicationContext(), "Mute",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        doneButton = (Button) view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowing = false;
                currScreen.onSettingsDonePressed();
                dismiss();
            }
        });

        musicDisableButton = (Button) view.findViewById(R.id.musicDisableButton);
        musicDisableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().muteMusic(true);
            }
        });

        musicEnableButton = (Button) view.findViewById(R.id.musicEnableButton);
        musicEnableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().muteMusic(false);
            }
        });


//        Utils.changeFont(getActivity().getAssets(), (TextView) view.findViewById(R.id.titleTextView));
//        Utils.changeFont(getActivity().getAssets(), (TextView)view.findViewById(R.id.volumeTextView));
//        Utils.changeFont(getActivity().getAssets(), (TextView)view.findViewById(R.id.musicTextView));

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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//
//    }

    @Override
    public void onStart(){
        super.onStart();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         @Override
                                         public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                                             if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                                 isShowing = false;
                                                 currScreen.onSettingsBackPressed();
//                                                 dismiss();
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     }
        );
    }
}
