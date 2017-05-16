package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;

/**
 * Created by iluxo on 16/05/2017.
 */

public class MessageGetter {

    private static final String LOG_TAG = MessageGetter.class.getName();

    private static final URL SSID_ENDPOINT = createNewUrl("http://10.0.2.2:8081/get/message/ssid");
    private static final URL GPS_ENDPOINT = createNewUrl("http://10.0.2.2:8081/get/message/gps");

    public static HashSet<Message> getSSIDMessages(List<String> mySSIDs) {
        JSONObject postDataParams = createAuthParams();
        try {
            postDataParams.put("my_ssids", new JSONArray(mySSIDs));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection conn = createSSIDServerConnection();

        HashSet<Message> messages = retrieveMessagesFromServer(conn, postDataParams);
        return messages;
    }

    private static HashSet<Message> retrieveMessagesFromServer(HttpURLConnection conn, JSONObject postDataParams) {
        String response = retrieveResponseFromServer(conn, postDataParams);
        HashSet<Message> messages = parseMessageListFromResponse(response);
        return messages;
    }

    private static HashSet<Message> parseMessageListFromResponse(String response) {
        HashSet<Message> messages = new HashSet<>();
        if (response.equals("401")){
            Log.e(LOG_TAG, "Cannot get SSID messages");
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("messages");
                for (int i = 0, size = jsonArray.length(); i < size; i++) {
                    JSONObject objectInArray = new JSONObject(jsonArray.getString(i));
                    messages.add(Message.fromJSONObject(objectInArray));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    private static String retrieveResponseFromServer(HttpURLConnection conn, JSONObject postDataParams) {
        try {
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sbuff = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    Log.d("Response:", line);
                    sbuff.append(line);
                    break;
                }

                in.close();
                return sbuff.toString();
            } else {
                return new String("" + responseCode);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
    }

    private static JSONObject createAuthParams() {
        JSONObject postDataParams = new JSONObject();

        try {
            postDataParams.put("username", LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postDataParams;
    }

    private static HttpURLConnection createSSIDServerConnection() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) SSID_ENDPOINT.openConnection();
            initConnection(conn);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static HttpURLConnection createGPSServerConnection() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) GPS_ENDPOINT.openConnection();
            initConnection(conn);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private static void initConnection(HttpURLConnection conn) throws ProtocolException {
        conn.setReadTimeout(15000 );
        conn.setConnectTimeout(15000 );
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
    }

    private static final URL createNewUrl(String url) {
        try {
            URL u = new URL(url);
            return u;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
