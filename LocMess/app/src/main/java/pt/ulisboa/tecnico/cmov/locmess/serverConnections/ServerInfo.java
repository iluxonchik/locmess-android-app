package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import java.net.HttpURLConnection;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public interface ServerInfo {
    //String SERVERINFO = "http://10.0.2.2:8081"; //Change this to local host
    String SERVERINFO = "http://192.168.1.69:8081"; //Change this to local host
    String REGISTERUSERURI = "/new/user";
    String LOGINURI = "/login";
    String LOGOUTURI = "/logout";
    String GETUSERKEYS = "/get/profile/keys";
    String KEYSUPDATE = "/profile/key/update";
    String KEYREMOVE = "/profile/key/delete";
    String ADDLOCATIONURI = "/new/location";
    String GETLOCATIONURI = "/get/location";
    String GETALLLOCURI = "/get/location/all";
    String GETMESSAGES = "/get/message/my";
    String SENDMESSAGE = "/new/message";
    String LOCATIONREMOVE = "/location/delete";
    String ADDWIFILOCURI = "/get/location/all";

    HttpURLConnection serverConnection();
}
