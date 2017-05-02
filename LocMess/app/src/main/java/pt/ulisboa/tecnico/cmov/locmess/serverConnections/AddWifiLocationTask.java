package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;

/**
 * Created by Roberto on 01/05/2017.
 */

public class AddWifiLocationTask extends AsyncTask<Object, Void, String> implements ServerInfo {
    Context context;
    URL url;

    public AddWifiLocationTask(Context context) {
        this.context = context;

        try {
            url = new URL(SERVERINFO + ADDLOCATIONURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected String doInBackground(Object... params) {
        /*
         * PARAM[0] - Name
         * PARAM[1] - List of wifi ID
         */
        JSONObject postDataParams = new JSONObject();
        JSONObject locationJson = new JSONObject();

        try {
            //Populating Location Json
            JSONArray arrayObj=new JSONArray();
            List <String> list = new ArrayList <String>();
            for (String r : (List<String>)params[1]) {
                arrayObj.put(r);
            }
            locationJson.put("ssids", arrayObj);

            //Populating PostData Json
            postDataParams.put("username", LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());
            postDataParams.put("name",(String)params[0]);
            postDataParams.put("is_gps", false);
            postDataParams.put("location_json", locationJson);


            HttpURLConnection conn = serverConnection();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return "";
            }
            else {
                //TODO: check this response
                return new String("false : "+responseCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpURLConnection serverConnection() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 );
            conn.setConnectTimeout(15000 );
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conn;
    }

}
