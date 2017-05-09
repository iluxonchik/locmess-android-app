package pt.ulisboa.tecnico.cmov.locmess.main;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.PeerListListener;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.WifiDirect.BroadcastMessageTask;
import pt.ulisboa.tecnico.cmov.locmess.WifiDirect.IncommingCommTask;
import pt.ulisboa.tecnico.cmov.locmess.WifiDirect.SimWifiP2pBroadcastReceiver;
import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.service.MessagePollingService;
import pt.ulisboa.tecnico.cmov.locmess.profile.MainProfileActivity;

public class MainMenuActivity extends AppCompatActivity implements PeerListListener {

    private final String LOG_TAG = MainMenuActivity.class.getName();

    public Context context;

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private SimWifiP2pBroadcastReceiver mReceiver;

    @Override
    protected void onRestart() {
        super.onRestart();

        LocalMemory.getInstance().getManager().populateLocations(this);
        LocalMemory.getInstance().getManager().populateKeys(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Log.d(LOG_TAG, "HEEEELLO");

        //setUpSharedPreferences();
        //startMessagePollingServiceAlarm();


        context=this;

        LocalMemory.getInstance().getManager().populateLocations(this);
        LocalMemory.getInstance().getManager().populateKeys(this);

        SimWifiP2pSocketManager.Init(getApplicationContext());

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        mReceiver = new SimWifiP2pBroadcastReceiver(this);
        registerReceiver(mReceiver, filter);
        enableWifiDirect();

        // spawn the server task
        new IncommingCommTask(context).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);

        // spawn the broadcast task
        new BroadcastMessageTask(context).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void startMessagePollingServiceAlarm() {
        Log.d(LOG_TAG, "startMessagePollingServiceAlarm():");
        if (!isMessagePollingServiceAlarmSetUp()) {
            Log.d(LOG_TAG, "Alarm is not set up, so let's do it");
            Intent intent = new Intent(this, MessagePollingService.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);

            // NOTE: aware of inexact timing
            // IMPORTANT: An alarm is a bad choice for this. Consider doing the delays in the
            // service itself? I
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 3); // TODO: fix hardcoded values
            long frequency = 3 * 1000;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                                                        frequency, pendingIntent);
            markMessagePollingServiceAlarmAsSetUp();
        }


    }

    private boolean isMessagePollingServiceAlarmSetUp() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(getString(R.string.alarm_set_up_shared_pref), false);
    }

    private void setUpSharedPreferences() {
        Log.d(LOG_TAG, "setUpSharedPreferences():");
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("sharedPrefAlreadySetUp", false)) {
            Log.d(LOG_TAG, "SharedPreferences not set up yet. Setting up...");

            // set up Shared Preferences, as it hasn't been done before
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.alarm_set_up_shared_pref), false);
            editor.commit();
        }
    }

    private void markMessagePollingServiceAlarmAsSetUp() {
        Log.d(LOG_TAG, "Marking alarm as set up...");
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                getString(R.string.shared_pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.alarm_set_up_shared_pref), true);
        editor.commit();
    }

    public void profile(View v) {
        Intent intent = new Intent(context, MainProfileActivity.class);
        context.startActivity(intent);
    }

    public void messages(View v) {
        LocalMemory.getInstance().getManager().populateMessages(this);
    }

    public void locations(View v) {
        Intent intent = new Intent(this, MainLocationsActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
        LocalMemory.getInstance().getManager().logout(this);
    }

    public void enableWifiDirect() {
        Intent intent = new Intent(this, SimWifiP2pService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mBound = true;
        LocalMemory.getInstance().setmBound(mBound);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // callbacks for service binding, passed to bindService()

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mManager = new SimWifiP2pManager(new Messenger(service));
            mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
            mBound = true;

            LocalMemory.getInstance().setmChannel(mChannel);
            LocalMemory.getInstance().setmManager(mManager);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mManager = null;
            mChannel = null;
            mBound = false;
        }
    };

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        StringBuilder peersStr = new StringBuilder();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
            peersStr.append(devstr);
        }

        // display list of devices in range
        new AlertDialog.Builder(this)
                .setTitle("Devices in WiFi Range")
                .setMessage(peersStr.toString())
                .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
