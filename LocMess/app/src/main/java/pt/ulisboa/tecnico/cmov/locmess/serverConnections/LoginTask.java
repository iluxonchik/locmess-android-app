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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public class LoginTask extends AsyncTask<String, Void, String> implements ServerInfo{
    Context context;
    URL url;

    private TaskDelegate delegate;

    public LoginTask(Context context, TaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
        try {
            url = new URL(SERVERINFO + LOGINURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... s) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put("username", s[0]);
            postDataParams.put("password", s[1]);
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


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                JSONObject jsonObj = null;
                while((line = in.readLine()) != null) {
                    if(line.startsWith("{")) jsonObj = new JSONObject(line);
                }

                in.close();

                return jsonObj.getString("token");
                //return sb.toString();
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
        delegate.LoginTaskComplete(s, context);
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
