package pt.ulisboa.tecnico.cmov.locmess.main;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import pt.ulisboa.tecnico.cmov.locmess.locations.InboxMessagesActivity;
import pt.ulisboa.tecnico.cmov.locmess.locations.LocationUpdaterService;
import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.service.MessagePollingService;
import pt.ulisboa.tecnico.cmov.locmess.profile.MainProfileActivity;

public class MainMenuActivity extends AppCompatActivity implements PeerListListener {

    private static final String LOG_TAG = MainMenuActivity.class.getName();
    private static final int LOCATION_PERMISSION_CODE = 1;

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
        setInboxMessagesCount();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setUpSharedPreferences();
        // startMessagePollingServiceAlarm();
        startLocationUpdatesListener();
        setInboxMessagesCount();

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

    @Override
    protected void onResume() {
        super.onResume();
        setInboxMessagesCount();
    }

    private void setInboxMessagesCount() {
        Button btn = (Button) findViewById(R.id.button_inbox_messages);
        LocalMemory localMemory = LocalMemory.getInstance();
        int inboxCount = localMemory.getNotYetAcceptedMessages().size();
        btn.setText("Inbox (" + inboxCount + ")");
    }

    private void startLocationUpdatesListener() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
        } else {
            startLocationUpdaterService();
        }
    }

    private void startLocationUpdaterService() {
        Intent i = new Intent(this, LocationUpdaterService.class);
        startService(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdaterService();
            } else {
                Snackbar.make(this.getCurrentFocus(), "The application needs location permission to function properly.", Snackbar.LENGTH_LONG).show();
            }
        }
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
            long frequency = 60 * 1000; // every minute
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
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

    public void startInboxActivity(View view) {
        Intent intent = new Intent(this, InboxMessagesActivity.class);
        startActivity(intent);
    }
}
