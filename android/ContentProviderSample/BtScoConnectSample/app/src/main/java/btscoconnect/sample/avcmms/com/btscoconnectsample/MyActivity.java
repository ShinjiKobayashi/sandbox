
package btscoconnect.sample.avcmms.com.btscoconnectsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyActivity extends Activity implements View.OnClickListener {
    private static final boolean DEBUG = false;
    private AudioManager mAudioManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothHeadset mBluetoothHeadset;
    private List<ConnectionData> mConnectionDeviceList = new ArrayList<ConnectionData>();
    private boolean mBtConnectionFlag = false;
    private ScoStateReceiver mReceiver = new ScoStateReceiver();
    private static Context mContext;
    private static MyActivity self;
    private TextToSpeech mTts = null;
    private TextView mTextView = null;

    private static final int MSG_HANDLE_TTS_START = 0;
    private static final int MSG_HANDLE_TXT_UPDATE = 1;
    private static final int MSG_HANDLE_SCO_TIMEOUT = 2;

    private void initTts() {
        mTts = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    public void onInit(int status) {
                        if (status != TextToSpeech.SUCCESS) {
                            // tts error!!
                            if (DEBUG)
                                Log.i("koba", "tts status error");
                        } else {
                            if (DEBUG)
                                Log.i("koba", "tts status success");
                            Locale l = Locale.ENGLISH;
                            if (mTts.isLanguageAvailable(l) >= TextToSpeech.LANG_AVAILABLE) {
                                mTts.setLanguage(l);
                            }
                        }
                    }
                });
    }

    Handler mUiThreadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (DEBUG)
                Log.i("koba", "getMessage : " + msg.what);
            switch (msg.what) {
                case MSG_HANDLE_TTS_START: {
                    if (DEBUG)
                        Log.i("koba", "tts speak");
                    mTts.speak(
                            "Hello, world. This is a test message.",
                            TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }
                case MSG_HANDLE_TXT_UPDATE: {
                    String message = (String) msg.obj;
                    String now = mTextView.getText().toString();

                    StringBuilder sb = new StringBuilder();
                    sb.append(now);
                    sb.append("\n");
                    sb.append(message);
                    mTextView.setText(sb.toString());
                    break;
                }
                case MSG_HANDLE_SCO_TIMEOUT: {
                    updateText("SCO Connection Timeout. Connection Not Response in 20sec");
                }
                default:
                    break;
            }
        }
    };

    static Context getActivityContext() {
        return MyActivity.mContext;
    }

    static MyActivity getActivityInstance() {
        return self;
    }

    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {

        /**
         * TODO: 複数接続時の処理対応
         * 
         * @param profile
         * @param proxy
         */
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {

            switch (profile) {
                case BluetoothProfile.HEADSET:
                    if (DEBUG)
                        Log.i("koba", "profile [HEADSET]");

                    mBluetoothHeadset = (BluetoothHeadset) proxy;
                    List<BluetoothDevice> devices = mBluetoothHeadset.getConnectedDevices();

                    for (BluetoothDevice device : devices) {
                        ConnectionData data = new ConnectionData();
                        data.setDeviceAddr(device.getAddress());
                        data.setDeviceName(device.getName());
                        mConnectionDeviceList.add(data);
                        if (DEBUG)
                            Log.i("koba", "[HEADSET] get device name : " + data.getDeviceName());

                        updateText(
                                "HEADSET Connection OK",
                                "DeviceName : " + data.getDeviceName(),
                                "DeviceAddr : " + data.getDeviceAddr()
                        );
                    }

                    if (mConnectionDeviceList.size() > 0) {
                        mBtConnectionFlag = true;
                    }

                    break;
                case BluetoothProfile.A2DP:
                    if (DEBUG)
                        Log.i("koba", "profile [A2DP]");
                    break;
                case BluetoothProfile.HEALTH:
                    if (DEBUG)
                        Log.i("koba", "profile [HEALTH]");
                    break;
                case BluetoothProfile.GATT:
                    if (DEBUG)
                        Log.i("koba", "profile [GATT]");
                    break;
                case BluetoothProfile.GATT_SERVER:
                    if (DEBUG)
                        Log.i("koba", "profile [GATT_SERVER]");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            // disconnectedの時の処理
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG)
            Log.i("koba", "--- onCreate");
        setContentView(R.layout.activity_my);

        this.mContext = getApplicationContext();
        self = this;
        ((Button) findViewById(R.id.button)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
        ((Button) findViewById(R.id.button3)).setOnClickListener(this);
        mTextView = (TextView) findViewById(R.id.textView);

        // 読み出し完了時にBluetoothProfile.ServiceListenerのonServiceConnectedがコールされる
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.HEADSET);
        mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.A2DP);
        mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.GATT);
        mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.GATT_SERVER);
        mBluetoothAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.HEALTH);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        initTts();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG)
            Log.i("koba", "--- onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG)
            Log.i("koba", "--- onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG)
            Log.i("koba", "--- onPause");
        mTts.shutdown();
        unregisterReceiver(mReceiver);
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
            case R.id.button: {
                handleButtonConnect();
                break;
            }
            case R.id.button2: {
                mUiThreadHandler.sendEmptyMessage(MSG_HANDLE_TTS_START);
                break;
            }
            case R.id.button3: {
                handleButtonDisconnectAction();
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * TextViewに出力するテキストを入力する。 1つのStringにつき、間に\nを含む出力を行う
     */
    private void updateText(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg);
            sb.append("\n");
        }

        Message msg = Message.obtain();
        msg.obj = sb.toString();
        msg.what = MSG_HANDLE_TXT_UPDATE;

        mUiThreadHandler.sendMessage(msg);
    }

    private void handleButtonDisconnectAction() {
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.stopBluetoothSco();
    }

    private void handleButtonConnect() {
        if (mBluetoothAdapter != null
                && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)
                    == BluetoothProfile.STATE_CONNECTED) {
            if (mAudioManager.isBluetoothScoAvailableOffCall()) {
                registerReceiver(mReceiver, new IntentFilter(
                        AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
                mAudioManager.setMode(AudioManager.MODE_IN_CALL);
                mAudioManager.setBluetoothScoOn(true);
                mAudioManager.startBluetoothSco();

                /**
                 * 20秒の固定タイムアウトを設ける。この間にReceiverがActionを受け取らなかったらエラー
                 */
                mUiThreadHandler.sendEmptyMessageDelayed(MSG_HANDLE_SCO_TIMEOUT, 20000);
                updateText("SCO Connection is Starting...");
            } else {
                if (DEBUG)
                    Log.i("koba", "SCO Not Available");

                updateText("SCO Connection is not available");
            }
        } else {
            if (DEBUG)
                Log.i("koba", "Bluetooth headset is not available.");

            updateText("Bluetooth headset is not available");
        }
    }

    public static class ScoStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DEBUG)
                Log.i("koba", "--- onReceive : " + action);
            int state = 0;
            if (action != null) {
                if (action.equals(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED)) {
                    getActivityInstance().mUiThreadHandler.removeMessages(MSG_HANDLE_SCO_TIMEOUT);
                    switch (getScoState(intent)) {

                        case AudioManager.SCO_AUDIO_STATE_CONNECTED: {
                            if (DEBUG)
                                Log.i("koba", "SCO_AUDIO_STATE_CONNECTED");

                            Toast.makeText(context, "sco connected!", Toast.LENGTH_SHORT).show();
                            getActivityInstance().updateText("SCO is Connected");
                            break;
                        }
                        case AudioManager.SCO_AUDIO_STATE_CONNECTING: {
                            if (DEBUG)
                                Log.i("koba", "SCO_AUDIO_STATE_CONNECTING");
                            Toast.makeText(context, "sco connecting!", Toast.LENGTH_SHORT).show();
                            getActivityInstance().updateText("SCO is Connecting");
                            break;
                        }
                        case AudioManager.SCO_AUDIO_STATE_DISCONNECTED: {
                            if (DEBUG)
                                Log.i("koba", "SCO_AUDIO_STATE_DISCONNECTED");
                            Toast.makeText(context, "sco disconnected!", Toast.LENGTH_SHORT).show();
                            getActivityInstance().updateText("SCO is Disconnected");
                            break;
                        }
                        case AudioManager.SCO_AUDIO_STATE_ERROR: {
                            if (DEBUG)
                                Log.i("koba", "SCO_AUDIO_STATE_ERROR");

                            Toast.makeText(context, "sco error!", Toast.LENGTH_SHORT).show();
                            getActivityInstance().updateText("SCO Connection has Error");
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        }

        /**
         * stateを取得して返す 初期状態はDISCONNECTED
         * 
         * @param intent
         * @return
         */
        private int getScoState(Intent intent) {
            return intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE,
                    AudioManager.SCO_AUDIO_STATE_DISCONNECTED);
        }
    }

}
