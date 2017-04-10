package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public interface TaskDelegate {
    void RegisterTaskComplete(String result, Context context);
    void LoginTaskComplete(String result, Context context);
}
