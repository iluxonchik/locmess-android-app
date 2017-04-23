package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import java.net.HttpURLConnection;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public interface ServerInfo {
    final String SERVERINFO = "http://10.0.2.2:8081"; //Change this to local host
    final String REGISTERUSERURI = "/new/user";
    final String LOGINURI = "/login";
    final String GETUSERKEYS = "/get/profile/keys";
    final String KEYSUPDATE = "/profile/key/update";
    final String ADDLOCATIONURI = "/new/location";
    final String GETLOCATIONURI = "/get/location";
    final String GETALLLOCURI = "/get/location/all";

    public HttpURLConnection serverConnection();
}
