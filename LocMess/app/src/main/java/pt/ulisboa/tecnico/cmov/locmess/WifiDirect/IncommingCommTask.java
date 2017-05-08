package pt.ulisboa.tecnico.cmov.locmess.WifiDirect;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.locmess.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Roberto Ponte on 5/8/2017.
 */

public class IncommingCommTask extends AsyncTask<Void, String, Void> {

    private SimWifiP2pSocketServer mSrvSocket = null;
    Context context;

    public IncommingCommTask(Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {
        //Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

        try {
            mSrvSocket = new SimWifiP2pSocketServer(10001);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SimWifiP2pSocket sock = mSrvSocket.accept();
                try {
                    BufferedReader sockIn = new BufferedReader(
                            new InputStreamReader(sock.getInputStream()));
                    String st = sockIn.readLine();
                    publishProgress(st);
                    sock.getOutputStream().write(("\n").getBytes());
                } catch (IOException e) {
                    Log.d("Error reading socket:", e.getMessage());
                } finally {
                    sock.close();
                }
            } catch (IOException e) {
                Log.d("Error socket:", e.getMessage());
                break;
                //e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d("RECEIVEDMESSAGE: ", values[0]);
        //TODO: Here verify the message and if is a message create notification.

        //Notification for alerting the user of new message received
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("New Message")
                        .setContentText("You receive a message from a close by neighbor.");

        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
