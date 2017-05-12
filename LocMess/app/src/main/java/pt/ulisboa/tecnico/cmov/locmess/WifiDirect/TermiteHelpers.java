package pt.ulisboa.tecnico.cmov.locmess.WifiDirect;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;

/**
 * Contains helper methods that simplify the interaction with the Termite's API.
 */
public final class TermiteHelpers {
    private final static String LOG_TAG = TermiteHelpers.class.getName();

    public static List<String> deviceListToSSIDList(SimWifiP2pDeviceList simWifiP2pDeviceList) {
        Collection<SimWifiP2pDevice> deviceList = simWifiP2pDeviceList.getDeviceList();
        List<String> ssids = new ArrayList<>(deviceList.size());

        if (deviceList.isEmpty()){
            Log.d(LOG_TAG, "Updated SSID list is empty");
        }

        for (SimWifiP2pDevice d: deviceList) {
            ssids.add(d.deviceName);
        }

        return ssids;
    }
}
