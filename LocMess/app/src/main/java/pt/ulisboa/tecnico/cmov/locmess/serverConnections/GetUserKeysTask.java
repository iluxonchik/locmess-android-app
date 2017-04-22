package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Valentyn on 22-04-2017.
 */

public class GetUserKeysTask extends AsyncTask<String, Void, List<String>> implements ServerInfo{
    Context context;
    URL url;

    private TaskDelegate delegate;

    public GetUserKeysTask(Context context, TaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
        try {
            url = new URL(SERVERINFO + GETUSERKEYS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> doInBackground(String... s) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("username", s[0]);
            postDataParams.put("token", s[1]);
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = serverConnection();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postDataParams.toString());

            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();

            List<String> l = new ArrayList<>();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                JSONObject jsonObj = null;
                while((line = in.readLine()) != null) {
                    if(line.startsWith("{")) jsonObj = new JSONObject(line);
                }

                in.close();

                while(jsonObj.keys().hasNext()) {
                    l.add((String) jsonObj.keys().next());
                }
                return l;
                //return sb.toString();
            }
            else {
                l.add("401");
                return l;
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
    protected void onPostExecute(List<String> l) {
        delegate.GetUserKeysTaskComplete(l, context);
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
