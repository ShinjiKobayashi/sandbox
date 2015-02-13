
package sample.toughpad.avcmms.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity implements View.OnClickListener {
    DeviceManagemetXml mXml;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mXml = new DeviceManagemetXml();
        ((Button) findViewById(R.id.button)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
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
                mXml.createInitXml();

                IpFilteringSettingsData data = new IpFilteringSettingsData();
                data.setToggle(true);
                data.setTargetWwan(true);
                data.setTargetEth(false);
                data.setTargetWifi(true);
                data.setTargetIPs("192.168.1.2/24,10.70.233.157/16");
                mXml.addIpFilteringRule(data);

                data.setTargetIPs("");
                mXml.addIpFilteringRule(data);

                ((TextView) findViewById(R.id.text)).setText(mXml.getXmlDataInString());
                break;
            }
            case R.id.button2: {
                mXml.deleteIpFilteringRule(0);
                ((TextView) findViewById(R.id.text)).setText(mXml.getXmlDataInString());
                break;
            }
        }
    }
}
