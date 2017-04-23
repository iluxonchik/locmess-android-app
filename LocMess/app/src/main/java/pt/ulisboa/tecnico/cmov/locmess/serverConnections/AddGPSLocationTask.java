package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public class AddGPSLocationTask extends AsyncTask<String, Void, String> implements ServerInfo {
    URL url;
    Context context;

    public AddGPSLocationTask(Context context) {

        this.context = context;

        try {
            url = new URL(SERVERINFO + ADDLOCATIONURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        /*
         * PARAM[0] - Name
         * PARAM[1] - Latitude
         * PARAM[2] - Longitude
         * PARAM[3] - Radius
         */
        JSONObject postDataParams = new JSONObject();
        JSONObject locationJson = new JSONObject();

        try {
            //Populating Location Json
            locationJson.put("latitude", params[1]);
            locationJson.put("longitude", params[2]);
            locationJson.put("radius", params[3]);

            //Populating PostData Json
            postDataParams.put("username",LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());
            postDataParams.put("name",params[0]);
            postDataParams.put("is_gps", "True");
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
                /*
                BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
                */
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
    protected void onPostExecute(String s) {
        //Toast.makeText(context, "was" + s , Toast.LENGTH_LONG).show();
        //Log.e("result",s);
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
