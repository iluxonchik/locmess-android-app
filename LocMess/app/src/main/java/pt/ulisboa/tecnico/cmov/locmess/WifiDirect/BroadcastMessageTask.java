package pt.ulisboa.tecnico.cmov.locmess.WifiDirect;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.locations.GetGpsLocation;
import pt.ulisboa.tecnico.cmov.locmess.locations.GpsLocation;
import pt.ulisboa.tecnico.cmov.locmess.locations.Location;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;

import static java.lang.Math.pow;

/**
 * Created by Roberto Ponte on 5/8/2017.
 */

public class BroadcastMessageTask
        extends AsyncTask<String, Void, String>
        implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    Context context;
    GetGpsLocation getGpsLocation;
    List<String> neighborsIp;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private String TAG = "WD";

    public BroadcastMessageTask(Context context) {
        this.context = context;
        getGpsLocation = new GetGpsLocation(context);
    }

    @Override
    protected String doInBackground(String... params) {
        while (!Thread.currentThread().isInterrupted()) {
            //###FIRST STEP
            //1st Get available peers, and if there is peers proceed
            mManager = LocalMemory.getInstance().getmManager();
            mChannel = LocalMemory.getInstance().getmChannel();
            if(mChannel != null && mManager != null){
                Log.d("X: ", "not null");

                if(LocalMemory.getInstance().ismBound()) {
                    //mManager.discoverPeers(mChannel,null);
                    mManager.requestGroupInfo(mChannel, this);
                } else
                    Log.d("X", "NOT BOUND");
                //2nd get decentralized messages
                List<Message> messages = LocalMemory.getInstance().getDecentralizedmessagesToSend();

                //3rd get my loc
                //3.1 GEO loc
                GpsLocation myLoc = new GpsLocation("myLoc", getGpsLocation.getLatitude(), getGpsLocation.getLongitude(), 0);
                //3.2 WIFI loc
                //4th checks messages that are supposed to be sent in this loc
                List<Message> messagesToBroadcast = messageToBroadcast(messages, myLoc);
                Log.d(TAG, "" +  messagesToBroadcast.size());
                //5th broadcast message to available
                if(neighborsIp != null) {
                    for (String ip : neighborsIp) {
                        for(Message m : messagesToBroadcast) {
                            try {
                                JSONObject toSend = new JSONObject();
                                toSend.put("MESSAGE", m.getJsonObject().toString());

                                SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(ip,10001 );
                                mCliSocket.getOutputStream().write((toSend.toString() + "\n").getBytes());
                                BufferedReader sockIn = new BufferedReader(new InputStreamReader(mCliSocket.getInputStream()));
                                sockIn.readLine();
                                mCliSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LocalMemory.getInstance().removeDescentralizedMessageToSend(m.getId());
                            LocalMemory.getInstance().addDecentralizedMessage(m);
                        }
                    }
                }
            }

            /*
            Log.d("X", "Neighbors part");
            if(neighborsIp != null) {
                for (String ip : neighborsIp) {
                    Log.d("X", ip);
                    try {
                        SimWifiP2pSocket mCliSocket = new SimWifiP2pSocket(ip,10001 );
                        mCliSocket.getOutputStream().write(("Hello" + "\n").getBytes());
                        BufferedReader sockIn = new BufferedReader(new InputStreamReader(mCliSocket.getInputStream()));
                        sockIn.readLine();
                        mCliSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }*/

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<Message> messageToBroadcast(List<Message> messages, GpsLocation loc) {

        Log.d(TAG + " LAT: ", "" + loc.getLatitude());
        Log.d(TAG + " LON: ", "" + loc.getLongitude());
        Log.d(TAG + " Radius: ", "" + loc.getRadious());

        List<Message> messagesToReturn = new ArrayList<>();
        for (Message m : messages) {
            String auxloc  = m.getLocation();
            GpsLocation messageLoc = (GpsLocation) LocalMemory.getInstance().getLocation(auxloc);

            Log.d(TAG + " LAT M: ", "" + messageLoc.getLatitude());
            Log.d(TAG + " LON M: ", "" + messageLoc.getLongitude());
            Log.d(TAG + " Radius M: ", "" + messageLoc.getRadious());

            double earthRadius = 6371000; //meters
            double dLat = Math.toRadians(loc.getLatitude()- messageLoc.getLatitude());
            double dLng = Math.toRadians(loc.getLongitude()- messageLoc.getLongitude());
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(loc.getLatitude())) * Math.cos(Math.toRadians(messageLoc.getLatitude())) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            float dist = (float) (earthRadius * c);

            if(dist <= messageLoc.getRadious())
                messagesToReturn.add(m);
        }
        return  messagesToReturn;
    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList) {

    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, SimWifiP2pInfo groupInfo) {
        neighborsIp = new ArrayList<>();
        if(groupInfo.getDevicesInNetwork() != null) {
            for (String deviceName : groupInfo.getDevicesInNetwork()) {
                SimWifiP2pDevice device = devices.getByName(deviceName);
                if(device!= null)
                    neighborsIp.add(device.getVirtIp());
            }
        }
    }
}
