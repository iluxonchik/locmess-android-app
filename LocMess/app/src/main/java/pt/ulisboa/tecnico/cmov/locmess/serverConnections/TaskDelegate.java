package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;

import java.util.List;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public interface TaskDelegate {
    void RegisterTaskComplete(String result, Context context);
    void LoginTaskComplete(String result, Context context);
    void LogoutTaskComplete(String result, Context context);
    void GetUserKeysTaskComplete(List<String> result, Context context);
    void UpdateKeyTaskComplete(String result, Context context);
    void RemoveKeyTaskComplete(String result, Context context);
    void SendMessageTaskComplete(String result, Context context);
    void RemoveLocationTaskComplete(String result, Context context);

}
