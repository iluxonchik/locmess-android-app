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
    }

    /*
    {
        "username":"thegame", "token":"gAAAAABY4bKwzNXJrqgZlh7F3W_MvZMB62HJc2ujLU9D-mfmp8Z6kI6zSnvTlOo-sKM3NERFo5jdCWjUMwKBU_3oZ2Ysq1AthQ==",
            "name":"Dre's House",
            "is_gps": "True",
            "location_json": "{\"latitude\":123, \"longitude\":456, \"radius\":1992}"
    }
    */


    @Override
    protected String doInBackground(String... params) {
        JSONObject postDataParams = new JSONObject();

        try {
            postDataParams.put("username",LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());
            postDataParams.put("name",params[0]);
            postDataParams.put("is_gps", "True");
            //TODO: AFTER ILLIYA AWNSER DO THIS PART

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

                BufferedReader in=new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {

                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();

            }
            else {
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
        Toast.makeText(context, "was" + s , Toast.LENGTH_LONG).show();
        Log.e("resutl",s);
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
