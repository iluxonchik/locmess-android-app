package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import java.net.HttpURLConnection;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public interface ServerInfo {
<<<<<<< Updated upstream
    final String SERVERINFO = "http://10.0.2.2:8081"; //Change this to local host
    final String REGISTERUSERURI = "/new/user";
    final String LOGINURI = "/login";
    final String LOGOUTURI = "/logout";
    final String GETUSERKEYS = "/get/profile/keys";
    final String KEYSUPDATE = "/profile/key/update";
    final String KEYREMOVE = "/profile/key/delete";
    final String ADDLOCATIONURI = "/new/location";
    final String GETLOCATIONURI = "/get/location";
    final String GETALLLOCURI = "/get/location/all";
    final String GETMESSAGES = "/get/message/my";
    final String SENDMESSAGE = "/new/message";
    final String LOCATIONREMOVE = "/location/delete";

=======
    String SERVERINFO = "http://10.0.2.2:8081"; //Change this to local host
    String REGISTERUSERURI = "/new/user";
    String LOGINURI = "/login";
    String LOGOUTURI = "/logout";
    String GETUSERKEYS = "/get/profile/keys";
    String KEYSUPDATE = "/profile/key/update";
    String KEYREMOVE = "/profile/key/delete";
    String ADDLOCATIONURI = "/new/location";
    String GETLOCATIONURI = "/get/location";
    String GETALLLOCURI = "/get/location/all";
    String ADDWIFILOCURI = "/get/location/all";
>>>>>>> Stashed changes

    HttpURLConnection serverConnection();
}
