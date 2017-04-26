package pt.ulisboa.tecnico.cmov.locmess.serverConnections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
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

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.MyDate;
import pt.ulisboa.tecnico.cmov.locmess.Message;


/**
 * Created by Valentyn on 26-04-2017.
 */

public class GetMessagesTask extends AsyncTask<String, Void, String> implements ServerInfo {

    Context context;
    URL url;

    public GetMessagesTask(Context context) {
        this.context = context;

        try {
            url = new URL(SERVERINFO + GETMESSAGES);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        JSONObject postDataParams = new JSONObject();

        try {
            postDataParams.put("username", LocalMemory.getInstance().getLoggedUserMail());
            postDataParams.put("token",LocalMemory.getInstance().getSessionKey());

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
                    Log.d("Response:", line);
                    sb.append(line);
                    break;
                }

                in.close();
                return sb.toString();
            }
            else {
                return new String(""+responseCode);
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
        try {
            JSONArray jsonArray = new JSONArray(s);
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject objectInArray = jsonArray.getJSONObject(i);

                List<String> prop = new ArrayList<>();
                MyDate startDate = new MyDate(0,0,0);
                MyDate endDate = new MyDate(0,0,0);

                objectInArray.getString("properties");
                objectInArray.getString("valid_from?");
                objectInArray.getString("valid_until?");

                LocalMemory.getInstance().addMessage(
                new Message(
                        Integer.parseInt(objectInArray.getString("msg_id")),
                        objectInArray.getString("title"),
                        objectInArray.getString("username"),
                        objectInArray.getString("location_name"),
                        objectInArray.getString("text"),
                        Boolean.valueOf(objectInArray.getString("is_centralized")),
                        Boolean.valueOf(objectInArray.getString("is_black_list")),
                        prop,
                        startDate,
                        endDate
                    )
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;
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
