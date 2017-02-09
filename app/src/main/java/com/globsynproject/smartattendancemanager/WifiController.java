package com.globsynproject.smartattendancemanager;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * Class that manages ALL WIFI-related jobs. This includes Access Points.
 */

public class WifiController {
    /**
     * WifiManager object that manages ALL wifi related functionality.
     */
    WifiManager wifiManager;

    /**
     * Constructor.
     * Initializes WifiManager Object.
     * @param context reference to calling object.
     */
    WifiController(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * Turns wifi ON.
     * @return boolean: true if the operation succeeds, else false.
     */
    public boolean turnWifiOn(){
        return wifiManager.setWifiEnabled(true);
    }

    /**
     * Turns wifi OFF.
     * @return boolean: true if the operation succeeds, else false.
     */
    public boolean turnWifiOff(){
        return wifiManager.setWifiEnabled(false);
    }

    /**
     * Connects to a particular network specified by the SSID and the Key (Password).
     * @param _SSID The name (SSID) of the connection to be connected to.
     * @param _key The Password of the connection to be connected to.
     * @return boolean: true if the operation succeeds, else false.
     */
    public boolean establishConnection(String _SSID, String _key){
        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = _SSID;
        wifiConfiguration.preSharedKey = _key;
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wifiManager.addNetwork(wifiConfiguration);
        return wifiManager.enableNetwork(wifiConfiguration.networkId, true);
    }

    /**
     * Disconnects from the connection presently connected to and removes it from the list of networks.
     * @return boolean: true if network is disconnected AND removed, else false.
     */
    public boolean disbandConnection(){
        WifiInfo info = wifiManager.getConnectionInfo();
        int id =info.getNetworkId();
        return wifiManager.disableNetwork(id) && wifiManager.removeNetwork(id);
    }
    /**
     * Turns access point on with the SSID as 'name' and Key as 'password'.
     * NOTE: Check the API Level for System Writer Permissions. If not Present then run the Commented Code.
     * @param name The name of the access point that is to be made.
     * @param password The password of the access point that is to be made.
     * @return WifiConfiguration: The object of WifiConfiguration that holds the settings of the previous access point (to be used to revert back to old settings when access point is turned off).
     */
    public WifiConfiguration turnAccessPointOn(String name, String password){
        /*if(!Settings.System.canWrite(context)){
            Intent i = new Intent().setAction(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            context.startActivity(i);
        }*/
        WifiConfiguration prev_config = null;
        try{
            wifiManager.setWifiEnabled(false);
            Method method1 = wifiManager.getClass().getMethod("getWifiApConfiguration");
            prev_config = (WifiConfiguration) method1.invoke(wifiManager);
            Message.logMessages("CONFIG_GET:", " " + prev_config.SSID);
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            WifiConfiguration config = new WifiConfiguration();
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.SSID = name;
            config.preSharedKey = password;
            method.invoke(wifiManager, config, true);
        }catch (Exception e){
            Message.logMessages("ERROR: ", e.toString());
            e.printStackTrace();
        }
        return prev_config;
    }

    /**
     * Turns access point off, reverting back to old settings of the user.
     * @param prev_config The previous access point configuration of the user to be set to.
     */
    public void turnAccessPointOff(WifiConfiguration prev_config){
        try {
            if(!(boolean)wifiManager.getClass().getMethod("isWifiApEnabled").invoke(wifiManager))
                return;
            Method method1 = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            method1.invoke(wifiManager, prev_config);
            Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifiManager, null, false);
        }catch (Exception e){
            Message.logMessages("ERROR: ", e.toString());
            e.printStackTrace();
        }
    }
}
