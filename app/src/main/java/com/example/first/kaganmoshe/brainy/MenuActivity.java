package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.CustomActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberGameActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HotAirBalloonActivity;

import java.util.Collections;


public class MenuActivity extends CustomActivity {

//    private TextView toolbarText;

    private static final String MENU_TOOLBAR_TEXT = "Menu";

    private ListView list;
    private String[] titles = {
            "Guess the Number",
            "HotAir Balloon",
            "Crazy Cube"
    };
    private Integer[] imageId = {
            R.drawable.hot_air_balloon,
            R.drawable.hot_air_balloon,
            R.drawable.hot_air_balloon
    };
    private String[] reviews = {
            "bla",
            "bla",
            "bla"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.setOnBackPressedActivity(SettingsActivity.class);

        MenuCustomList adapter = new
                MenuCustomList(MenuActivity.this, titles, imageId, reviews);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Class cls = null;

                switch (titles[+position]) {
                    case "Guess the Number":
                        cls = GuessTheNumberConfigActivity.class;
//                        onGuessTheNumberClick();
                        break;
                    case "HotAir Balloon":
                        cls = HotAirBalloonActivity.class;
//                        onHotAirBalloonClick();
                        break;
                    case "Crazy Cube":
                        cls = CrazyCubeActivity.class;
//                        onCrazyCubeClick();
                        break;
                }

                Utils.startNewActivity((Activity)view.getContext(), cls);
            }
        });
    }


//    private Button m_CrazyCubeBtn;
//    private Button m_GuessTheNumberBtn;
//    private Button m_SettingsBtn;
//    private Button m_HotAirBalloonBtn;
//    private TextView toolbarText;
//
//    private static final String MENU_TOOLBAR_TEXT = "Menu";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu);
//
//        initViewActivity();
//    }
//
//    private void initViewActivity() {
//        toolbarText = (TextView) findViewById(R.id.toolbarText);
//        toolbarText.setText(MENU_TOOLBAR_TEXT);
//
//        m_CrazyCubeBtn = (Button) findViewById(R.id.CrazyCubeBtn);
//        m_CrazyCubeBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                onCrazyCubeClick();
//            }
//        });
//
//        m_GuessTheNumberBtn = (Button) findViewById(R.id.guessTheNumberBtn);
//        m_GuessTheNumberBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onGuessTheNumberClick();
//            }
//        });
//
//        m_SettingsBtn = (Button) findViewById(R.id.settingsBtn);
//        m_SettingsBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onSettingsClick();
//            }
//        });
//
//        m_HotAirBalloonBtn = (Button) findViewById(R.id.hotAirBalloonBtn);
//        m_HotAirBalloonBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                onHotAirBalloonClick();
//            }
//        });
//    }
//
    private void onHotAirBalloonClick() {
        Intent intent = new Intent(this, HotAirBalloonActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onCrazyCubeClick() {
        //TODO - make code reusable for every click
        Intent intent = new Intent(this, CrazyCubeActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onSettingsClick() {
        //TODO - Update settings
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onGuessTheNumberClick() {
        Intent intent = new Intent(this, GuessTheNumberConfigActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
