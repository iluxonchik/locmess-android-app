package pt.ulisboa.tecnico.cmov.locmess.messages.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

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

    public MessagePollingService(String name) {
        super(name);
    }

    public MessagePollingService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
