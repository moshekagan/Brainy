package com.example.first.kaganmoshe.brainy.HotAirBallon;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.first.kaganmoshe.brainy.R;

import java.util.LinkedList;
import java.util.List;

public class HotAirBalloonActivity extends Activity {

    // Data Members
    private final String HOT_AIR_BALLON = "HotAirBalloon";
    private ImageView balloonImageView;
    private int balloonFrameHeight;

    // Temporery Members
    private Button animateBtn;
    private List<Integer> attValues = new LinkedList<>();
    private int i = 0;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_air_balloon);

        initAttentionList();

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        final int activityWidth = size.x;
//        activityHeight = size.y;

        FrameLayout balloonFrame = (FrameLayout) findViewById(R.id.airBalloonFrameLayout);
        balloonFrameHeight = balloonFrame.getHeight();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hot_air_ballon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initAttentionList(){
        attValues.add(50);
        attValues.add(0);
        attValues.add(70);
        attValues.add(60);
        attValues.add(90);
        attValues.add(80);
        attValues.add(100);
    }

}
