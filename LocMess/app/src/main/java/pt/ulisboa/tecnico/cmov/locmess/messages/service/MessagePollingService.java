package pt.ulisboa.tecnico.cmov.locmess.messages.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.messages.notifications.NewMessageNotificationReceiverActivity;

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
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private final static String SERVICE_NAME = MessagePollingService.class.getName();
    private final static String LOG_TAG = SERVICE_NAME;

    public MessagePollingService(String name) {
        super(name);
    }

    public MessagePollingService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "onHandleIntent()");
        // poll server for messages in my area

        // if message is not in my list already, show it to the user
        // in a notification

        // if the user accepts the message, add it to my messages

        // ** Notificaiton Building ** //
        NotificationManager notificationManager =
                                  (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);

        // intent that's triggered if the notification is selected
        Intent notifIntent = new Intent(this, NewMessageNotificationReceiverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                (int)System.currentTimeMillis(), notifIntent, 0);

        Notification notif = new NotificationCompat.Builder(this)
                            .setContentTitle("Important question")
                            .setContentText("Would you do it if my name was Dre?")
                            .setSmallIcon(R.drawable.ic_locmess)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            //.addAction(R.drawable.ic_locmess, "Action 1", newPendingIntent)
                            .build();

        notificationManager.notify(0, notif);

        Log.d(LOG_TAG, "Notification sent");
    }
}
