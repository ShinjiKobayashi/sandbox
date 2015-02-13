package sample.toughpad.avcmms.com.accessorysample;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;


public class MyActivity extends Activity {

    UsbManager mUsbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> map = mUsbManager.getDeviceList();
        Log.i("koba", "hashmap : " + map.toString());

        UsbAccessory[] accessoryList = mUsbManager.getAccessoryList();
        for(UsbAccessory accessory :accessoryList ){
            Log.i("koba", "manifacturer : " + accessory.getManufacturer());
        }

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
}
