package pt.ulisboa.tecnico.cmov.locmess.messages.notifications;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.locmess.R;

public class NewMessageNotificationReceiverActivity extends AppCompatActivity {
    private final String LOG_TAG = NewMessageNotificationReceiverActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message_notification_receiver);
        Log.d(LOG_TAG, "onCreate()");
    }
}
