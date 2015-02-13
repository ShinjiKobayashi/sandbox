
package sample.toughpad.avcmms.com.wwantestsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyActivity extends Activity implements View.OnClickListener {

    private final static String TARGET_DEVICE_PATH = "/dev/ttyUSB2";
    private TextView mTextView;
    private Handler uiHandler;
    private Handler senderHandler;
    private HandlerThread senderThread;
    private Looper mSenderThreadLooper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        ((Button) findViewById(R.id.button)).setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView2);

        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String str = (String)msg.obj;
                mTextView.setText(mTextView.getText() + str);
            }
        };

        senderThread = new HandlerThread("sender thread");
        senderThread.start();
        mSenderThreadLooper = senderThread.getLooper();
        senderHandler = new Handler(mSenderThreadLooper);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread thread = new Receiver();
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSenderThreadLooper.quit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                handleEnterButton();
                break;
            default:
                break;
        }
    }

    private void handleEnterButton() {
        final String cmd = ((EditText) findViewById(R.id.editText)).getText().toString();

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText("[send] command : " + cmd + "\n");
            }
        });
        senderHandler.post(new Runnable() {
            @Override
            public void run() {
                sendAtCommand(cmd);
            }
        });

    }

    private synchronized void sendAtCommand(String cmd) {

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(TARGET_DEVICE_PATH));
            bw.write(cmd + "\r");
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            Log.i("koba", "★sendAtCommand fail > " + cmd);
            e.printStackTrace();
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Receiver extends Thread {
        @Override
        public void run() {
            Log.i("koba","--------> run in");
            BufferedReader br = null;
            String str;
            try {
                br = new BufferedReader(new FileReader(TARGET_DEVICE_PATH));

                while ((str = br.readLine()) != null) {
                    Log.d("koba", "ret readLine str:" + str);

                    Message msg = uiHandler.obtainMessage();
                    msg.obj = str;
                    uiHandler.sendMessage(msg);

                    if (Thread.interrupted()) {
                        Log.d("koba", "thread is interrupted");
                        throw new InterruptedException();
                    }
                }
            } catch (IOException e) {
                System.out.println("I/O error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i("koba","--------> run out");
        }
    }
}
