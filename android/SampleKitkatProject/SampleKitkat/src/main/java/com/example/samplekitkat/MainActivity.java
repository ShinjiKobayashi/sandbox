package com.example.samplekitkat;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
    private final String TAG = "SampleKitkat";

    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if device is memory constrained
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        boolean lowRam = activityManager.isLowRamDevice();

        if(lowRam){
            // Modify memory use behavior
            Log.i(TAG,"low memory device");
        }



        ((Button)findViewById(R.id.button01)).setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        // when the window loses focus,
        // cancel any pending hide action. When the window gains focus,
        // hide the system UI

        if(hasFocus){
//            delayedHideSystemUI(3000);
        }else{
            mHideSystemUiHandler.removeMessages(0);
        }


    }

    private void hideSystemUISticky(){
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void hideSystemUI(){
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE );
    }

    private void showSystemUI(){
        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
    }

    private void delayedHideSystemUI(int delayMillis){
        mHideSystemUiHandler.removeMessages(0);
        mHideSystemUiHandler.sendEmptyMessageDelayed(0, delayMillis);
    }

    Handler mHideSystemUiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideSystemUI();
        }
    };



    private long cnt = 0;
    @Override
    public void onClick(View v) {
        if(cnt%2 == 0){
            hideSystemUISticky();
        }else{
            showSystemUI();
        }
        cnt++;
    }
}
