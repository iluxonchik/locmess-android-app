package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;

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
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Valentyn on 27-04-2017.
 */

public class SendMessageTask extends AsyncTask<Object, Void, String> implements ServerInfo {
    URL url;
    Context context;

    private TaskDelegate delegate;

    public SendMessageTask(Context context, TaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
        try {
            url = new URL(SERVERINFO + SENDMESSAGE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Object... s) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("username", s[0]);
            postDataParams.put("token", s[1]);
            postDataParams.put("title", s[2]);
            postDataParams.put("location_name", s[3]);
            postDataParams.put("text", s[4]);
            postDataParams.put("is_centralized", s[5]);
            postDataParams.put("is_black_list", s[6]);
            List<String> l = (List<String>)s[7];
            JSONObject propi = new JSONObject();
            for(String x : l){
                propi.put(x.split(":")[0],x.split(":")[1]);
            }
            postDataParams.put("properties", propi);
            if(!s[8].equals(""))
                postDataParams.put("valid_from", s[8]);
            if(!s[9].equals(""))
                postDataParams.put("valid_until", s[9]);

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
                return new String("" + responseCode);
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
        delegate.SendMessageTaskComplete(s, context);
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

