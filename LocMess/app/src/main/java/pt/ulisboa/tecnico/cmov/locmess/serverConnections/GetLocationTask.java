package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;

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

import javax.net.ssl.HttpsURLConnection;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;

/**
 * Created by Roberto Ponte on 4/10/2017.
 */

public class GetLocationTask extends AsyncTask<String, Void, String> implements ServerInfo {

    Context context;
    URL url;

    public GetLocationTask(Context context) {
        this.context = context;
        try {
            url = new URL(SERVERINFO + GETLOCATIONURI);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        /*
         *Param[0] - Name of the location
         */

        JSONObject postDataParams = new JSONObject();
        try{
            //Populating PostData Json
            postDataParams.put("username", LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());
            postDataParams.put("name",params[0]);

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
        } catch (Exception e){}



        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        //TODO: Update interface 
        super.onPostExecute(s);

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
