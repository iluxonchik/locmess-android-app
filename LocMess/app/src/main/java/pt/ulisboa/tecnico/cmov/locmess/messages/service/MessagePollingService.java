package pt.ulisboa.tecnico.cmov.locmess.messages.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.HashSet;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.locations.InboxMessagesActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;
import pt.ulisboa.tecnico.cmov.locmess.messages.notifications.NewMessageNotificationReceiverActivity;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.MessageGetter;

/**
 * Created by The Protégé Of The D.R.E. on 07/05/2017.
 * (since everyone else left those in their files, I'll leave a mark too ;)
 */

/**
 * Service that polls for messages in User's location (both: SSID and GPS) and shows him the
 * notification which asks him if he wants to accept or reject/ignore the message.
 *
 * Please note, this service does not restart automatically, you'll have to do it by yourself.
 */
public final class MessagePollingService extends IntentService {
    public final static String LOCATION_TYPE_EXTRA = "type";
    public final static String SSID_LIST_EXTRA = "ssid_list";

    public final static String LATITUDE_EXTRA = "latitude";
    public final static String LONGITUDE_EXTRA = "longitude";

    public enum LocationType {GPS, SSID};

    private final static String SERVICE_NAME = MessagePollingService.class.getName();

    private final static String LOG_TAG = SERVICE_NAME;
    private static boolean isWifiRegistered = false;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MessagePollingService(String name) {
        super(name);
    }

    public MessagePollingService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent()");
        LocationType locType = (LocationType)intent.getExtras().get(LOCATION_TYPE_EXTRA);

        if (locType.equals(LocationType.SSID)) {
            handleSSIDMessageLocaion(intent);
        } else {
            handleGPSMessageLocation(intent);
        }

        // poll server for messages in my area

        // if message is not in my list already, show it to the user
        // in a notification

        // if the user accepts the message, add it to my messages


    }

    private void handleGPSMessageLocation(Intent intent) {
        Log.d(LOG_TAG, "Handling GPS message location");

        double latitude = intent.getDoubleExtra(MessagePollingService.LATITUDE_EXTRA, 0);
        double longitude = intent.getDoubleExtra(MessagePollingService.LONGITUDE_EXTRA, 0);
        Log.d(LOG_TAG, "Latitude: " + latitude + " Longitude: " + longitude);
        HashSet<Message> messageHashSet = MessageGetter.getGPSMessages(latitude, longitude);
        treatNewMessagesAvailable(messageHashSet);
    }

    private void handleSSIDMessageLocaion(Intent intent) {
        Log.d(LOG_TAG, "Handling SSID message location");

        List<String> ssids = (List<String>) intent.getExtras().get(SSID_LIST_EXTRA);
        Log.d(LOG_TAG, "Got SSID list: " + ssids.toString());

        HashSet<Message> messageHashSet = MessageGetter.getSSIDMessages(ssids);

        treatNewMessagesAvailable(messageHashSet);
    }

    private void treatNewMessagesAvailable(HashSet<Message> messageHashSet) {
        LocalMemory locMem = LocalMemory.getInstance();
        locMem.addNotYetAcceptedMessages(messageHashSet);
        int size = locMem.getNotYetAcceptedMessages().size();

        if (size < 1) {
            // no new messages available, don't show notification
            return;
        }

        String title;
        String text;

        if (size > 1) {
            title = "New Messages Available";
            text =  size + " new messages available!";
        } else {
            title = "New Message Available";
            text = "1 new message available!";
        }

        showNotificaiton(title, text);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "Destroying service...");
        //Toast.makeText(this, "Destroy serivice...", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    private void showNotificaiton(String title, String text) {
        // ** Notificaiton Building ** //
        NotificationManager notificationManager =
                (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);

        // intent that's triggered if the notification is selected
        Intent notifIntent = new Intent(this, InboxMessagesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                (int)System.currentTimeMillis(), notifIntent, 0);

        Notification notif = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_locmess)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                //.addAction(R.drawable.ic_locmess, "Action 1", pendingIntent)
                .build();

        notificationManager.notify(0, notif);

        Log.d(LOG_TAG, "Notification sent");
    }
}
