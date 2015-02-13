
package sample.toughpad.avcmms.com.myapplication;

import java.util.Arrays;
import java.util.List;

/**
 * Created by skobayashi on 6/10/14.
 */
public class IpFilteringSettingsData {
    private int BIT_INTERFACE_WWAN = 0x0001;
    private int BIT_INTERFACE_WIFI = 0x0002;
    private int BIT_INTERFACE_WLAN = 0x0004;

    private boolean mToggle;
    private boolean mTargetWifi;
    private boolean mTargetWwan;
    private boolean mTargetEth;
    private String mTargetIPs;
    private String mTargetNetworks;

    public boolean isToggle() {
        return mToggle;
    }

    public void setToggle(boolean mToggle) {
        this.mToggle = mToggle;
    }

    public String getTargetNetworks() {
        return mTargetNetworks;
    }

    public void setTargetNetworks(String mTargetNetworks) {
        this.mTargetNetworks = mTargetNetworks;
    }

    public void setTargetIPs(String mTargetIPs) {
        this.mTargetIPs = mTargetIPs;
    }

    public String getTargetIPs() {
        return mTargetIPs;
    }

    public boolean isTargetWifi() {
        return mTargetWifi;
    }

    public void setTargetWifi(boolean mTargetWifi) {
        this.mTargetWifi = mTargetWifi;
    }

    public boolean isTargetWwan() {
        return mTargetWwan;
    }

    public void setTargetWwan(boolean mTargetWwan) {
        this.mTargetWwan = mTargetWwan;
    }

    public boolean isTargetEth() {
        return mTargetEth;
    }

    public void setTargetEth(boolean mTargetEth) {
        this.mTargetEth = mTargetEth;
    }

    public List<String> getWhiteList() {
        if (mTargetIPs == null) {
            return null;
        }

        String ips[] = mTargetIPs.split(",", -1);
        return Arrays.asList(ips);
    }

    public int getInterfaceCode(){
        int ret = 0;
        if(mTargetWwan){
            ret |= BIT_INTERFACE_WWAN;
        }
        if(mTargetWifi){
            ret |= BIT_INTERFACE_WIFI;
        }
        if(mTargetEth){
            ret |= BIT_INTERFACE_WLAN;
        }
        return ret;
    }

}
