package pt.ulisboa.tecnico.cmov.locmess.WifiDirect;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.main.MainMenuActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.service.MessagePollingService;

/**
 * Created by Roberto on 07/05/2017.
 */

public class SimWifiP2pBroadcastReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = SimWifiP2pBroadcastReceiver.class.getName();
    private MainMenuActivity mActivity;

    public SimWifiP2pBroadcastReceiver(MainMenuActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // This action is triggered when the Termite service changes state:
            // - creating the service generates the WIFI_P2P_STATE_ENABLED event
            // - destroying the service generates the WIFI_P2P_STATE_DISABLED event

            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE, -1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(mActivity, "WiFi Direct enabled",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WiFi Direct disabled",
                        Toast.LENGTH_SHORT).show();
            }

        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()

            Toast.makeText(context, "Peer list changed",
                    Toast.LENGTH_SHORT).show();
            SimWifiP2pManager man = LocalMemory.getInstance().getmManager();
            man.requestPeers(LocalMemory.getInstance().getmChannel(), new SimWifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {
                    // From here on, start an IntentService to go and update the message list
                    // for the device's new SSID list

                    List<String> ssids = TermiteHelpers.deviceListToSSIDList(simWifiP2pDeviceList);
                    Intent i = new Intent(context, MessagePollingService.class);
                    i.putExtra(MessagePollingService.LOCATION_TYPE_EXTRA,
                                                        MessagePollingService.LocationType.SSID);
                    i.putExtra(MessagePollingService.SSID_LIST_EXTRA, (Serializable) ssids);
                    context.startService(i);
                }
            });

        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {

            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
                    SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            ginfo.print();
            Toast.makeText(context, "Network membership changed",
                    Toast.LENGTH_SHORT).show();

        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {

            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(
                    SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            ginfo.print();
            Toast.makeText(context, "Group ownership changed",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
