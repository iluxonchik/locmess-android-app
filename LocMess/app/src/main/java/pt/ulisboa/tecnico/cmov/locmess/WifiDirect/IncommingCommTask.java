package pt.ulisboa.tecnico.cmov.locmess.WifiDirect;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketServer;
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Roberto Ponte on 5/8/2017.
 */

public class IncommingCommTask extends AsyncTask<Void, String, Void> {

    private SimWifiP2pSocketServer mSrvSocket = null;
    Context context;
    int mNotificationId = 0;

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

        try {
            JSONObject messageReceived = new JSONObject(values[0]);
            JSONObject messageJson = new JSONObject(messageReceived.getString("MESSAGE"));
            int id = messageJson.getInt("id");
            String title = messageJson.getString("title");
            String author = messageJson.getString("author");
            String location = messageJson.getString("location");
            String text = messageJson.getString("text");
            Boolean isCentralized = messageJson.getBoolean("isCentralized");
            Boolean isBlacklist = messageJson.getBoolean("isBlackList");
            List<String> keys = Arrays.asList(messageJson.getString("keys").replace("[","").replace("]","").split(","));
            String sDate = messageJson.getString("eDate");
            String eDate = messageJson.getString("sDate");

            String TAG = "JSON FIELDS ";
            Log.d(TAG, "ID " + id);
            Log.d(TAG, "Title " + title);
            Log.d(TAG, "Author " + author);
            Log.d(TAG, "Location " + location);
            Log.d(TAG, "Text " + text);
            Log.d(TAG, "IsCentralized " + isCentralized);
            Log.d(TAG, "IsBlacklist " + isBlacklist);
            Log.d(TAG, "Keys " + keys.toString());
            Log.d(TAG, "sDate " + sDate);
            Log.d(TAG, "eDate " + eDate);

            Message m = new Message(id, title, author, location, text, isCentralized, isBlacklist, keys, sDate, eDate);

            LocalMemory.getInstance().addDecentralizedMessage(m);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Notification for alerting the user of new message received
        //TODO: make it possible for the user to accpet or reject the message
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("New Message")
                        .setContentText("You receive a message from a close by neighbor.");

        // Sets an ID for the notification
        mNotificationId ++;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }


}
