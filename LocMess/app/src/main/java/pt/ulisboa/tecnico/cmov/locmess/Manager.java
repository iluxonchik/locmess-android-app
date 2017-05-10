package pt.ulisboa.tecnico.cmov.locmess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.locations.GpsLocation;
import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;
import pt.ulisboa.tecnico.cmov.locmess.locations.WifiLocation;
import pt.ulisboa.tecnico.cmov.locmess.main.LogInActivity;
import pt.ulisboa.tecnico.cmov.locmess.main.MainMenuActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.MainMessagesActivity;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;
import pt.ulisboa.tecnico.cmov.locmess.profile.MainProfileActivity;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.AddGPSLocationTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.AddWifiLocationTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.GetAllLocationsTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.GetMessagesTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.GetUserKeysTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.LoginTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.LogoutTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.RegisterTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.RemoveKeyTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.RemoveLocationTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.RemoveMessageTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.SendMessageTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.TaskDelegate;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.UpdateKeyTask;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Valentyn on 23-03-2017.
 */

public class Manager implements TaskDelegate{

    ProgressDialog progress;
    public Manager() {
    }

    public void login(Context context , String user, String pass){
        LocalMemory.getInstance().setLoggedUserMail(user);
        LocalMemory.getInstance().setLoggedUserPass(pass);

        LoginTask loginTask = new LoginTask(context, this);
        loginTask.execute(user,pass);

    }

    public void logout(Context context){
        LogoutTask logoutTask = new LogoutTask(context, this);
        logoutTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey());
    }


    public void register(Context context, String user, String pass, String confirm_pass){
        if(pass.length()<6) {
            Toast.makeText(context, "The password must have at least 6 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!confirm_pass.equals(pass)) {
            Toast.makeText(context, "Password do not match.", Toast.LENGTH_LONG).show();
            return;
        }

        LocalMemory.getInstance().setLoggedUserMail(user);
        LocalMemory.getInstance().setLoggedUserPass(pass);

        RegisterTask registerTask = new RegisterTask(context, this);
        registerTask.execute(user, pass);


    }

    public boolean addGpsLocation(Context context, String name, String latitude, String longitude, String radius){
        if(name.equals("") ) {
            Toast.makeText(context, "Please insert a name", Toast.LENGTH_LONG).show();
            return false;
        } else if(radius.equals("0.0")){
            Toast.makeText(context, "Please insert a radius", Toast.LENGTH_LONG).show();
            return false;
        }
        //TODO: Verify if the response was successfull and than add to the local memory
        LocalMemory.getInstance().addLocation(new GpsLocation(name,Double.parseDouble(latitude),Double.parseDouble(longitude),Double.parseDouble(radius)));

        AddGPSLocationTask addGPSLocationTask = new AddGPSLocationTask(context);
        addGPSLocationTask.execute(name, latitude, longitude, radius);
        return true;
    }

    public void addWifiLocation(Context context , String name, List<String> foundDevices){
        LocalMemory.getInstance().addLocation(new WifiLocation(name,foundDevices));

        AddWifiLocationTask addWifiLocationTask = new AddWifiLocationTask(context);
        addWifiLocationTask.execute(name, foundDevices);

        Intent intent = new Intent(context, MainLocationsActivity.class);
        context.startActivity(intent);
    }

    public void sendMessage(Context context,String title,String location,String text,boolean isCentralized,
                            boolean isBlackList, List<String> keys, String sDate,String eDate){
        SendMessageTask sendMessageTask = new SendMessageTask(context, this);
        sendMessageTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey(),
                title,location,text,isCentralized,isBlackList,keys,sDate,eDate);
    }

   public void decentralizedMessageToSend(Context context, String title,String location,String text,boolean isCentralized,
                                    boolean isBlackList, List<String> keys, String sDate,String eDate) {
        int id = LocalMemory.getInstance().getIdCounter();

       Message m = new Message(id, title, LocalMemory.getInstance().getLoggedUserMail(), location, text,
               isCentralized, isBlackList, keys, sDate, eDate);
       LocalMemory.getInstance().addDecentralizedMessageToSend(m);

       LocalMemory.getInstance().decrementId();

       Activity a = (Activity) context;
       a.setResult(RESULT_OK, null);
       a.finish();

       Intent intent = new Intent(context, MainMessagesActivity.class);
       context.startActivity(intent);
   }

    public void populateKeys(Context context){
        GetUserKeysTask userKeysTask = new GetUserKeysTask(context, this);
        userKeysTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey());
    }

    public void populateMessages(Context context){
        GetMessagesTask getMessagesTask = new GetMessagesTask(context);
        getMessagesTask.execute();
    }

    public void populateLocations(Context context){
        GetAllLocationsTask getAllLocationsTask = new GetAllLocationsTask(context);
        getAllLocationsTask.execute();
    }

    public void updateKey(Context context , String key, String value){
        UpdateKeyTask updateKeyTask = new UpdateKeyTask(context, this);
        updateKeyTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey(),key,value);
    }

    public void removeLocation(Context context , String name){

        RemoveLocationTask removeLocationTask = new RemoveLocationTask(context, this);
        removeLocationTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey(),name);
    }

    public void removeMessage(Context context , int id){
        boolean isDecentralized=false;
        for (Message m : LocalMemory.getInstance().getDecentralizedMessages()){
            if (m.getId() == id){
                isDecentralized = true;
                break;
            }
        }

        if (isDecentralized){
            LocalMemory.getInstance().removeDescentralizedMessage(id);

            //LocalMemory.getInstance().getManager().populateMessages(context);
        }
        else {
            RemoveMessageTask removeMessageTask = new RemoveMessageTask(context, this);
            removeMessageTask.execute(LocalMemory.getInstance().getLoggedUserMail(), LocalMemory.getInstance().getSessionKey(), "" + id);
        }

        Intent intent = new Intent(context, MainMessagesActivity.class);
        context.startActivity(intent);

        Activity a = (Activity) context;
        a.finish();

    }

    public void removeKey(Context context , String key,String value){

        RemoveKeyTask removeKeyTask = new RemoveKeyTask(context, this);
        removeKeyTask.execute(LocalMemory.getInstance().getLoggedUserMail(),LocalMemory.getInstance().getSessionKey(),key,value);
    }


    @Override
    public void RegisterTaskComplete(String result, Context context) {
        if(result.equals("401")){
            LocalMemory.getInstance().setLoggedUserMail("");
            LocalMemory.getInstance().setLoggedUserPass("");
            Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
        } else {
            Activity a = (Activity) context;
            a.setResult(RESULT_OK, null);
            a.finish();

            LoginTask loginTask = new LoginTask(context, this);
            loginTask.execute(LocalMemory.getInstance().getLoggedUserMail(), LocalMemory.getInstance().getLoggedUserPass());
        }
    }

    @Override
    public void LoginTaskComplete(String result, Context context) {
        if(result.equals("401")){
            LocalMemory.getInstance().setLoggedUserMail("");
            LocalMemory.getInstance().setLoggedUserPass("");
            Toast.makeText(context, "Wrong Login information", Toast.LENGTH_LONG).show();
        } else {
            LocalMemory.getInstance().setSessionKey(result);

            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            context.startActivity(intent);

            Activity a = (Activity) context;
            a.finish();
        }
    }

    @Override
    public void LogoutTaskComplete(String result, Context context) {
        if(result.equals("401"))
            Toast.makeText(context, "Some problem occurred in logout.", Toast.LENGTH_LONG).show();

        LocalMemory.getInstance().setLoggedUserMail("");
        LocalMemory.getInstance().setLoggedUserPass("");

        Intent intent = new Intent(context, LogInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        context.startActivity(intent);

        Activity a = (Activity) context;
        a.finish();
    }

    @Override
    public void GetUserKeysTaskComplete(List<String> result, Context context) {
        if(result!=null && (result.size()>0 && result.get(0).equals("401"))){
            Toast.makeText(context, "Cannot load user keys", Toast.LENGTH_LONG).show();
        } else {
            if(result!=null){
                LocalMemory.getInstance().loadKeys(result);
            }
        }
    }

    @Override
    public void UpdateKeyTaskComplete(String result, Context context) {
        if(result.equals("401")){
            Toast.makeText(context, "Cannot add the key pair.", Toast.LENGTH_LONG).show();
        } else {
            Activity a = (Activity) context;
            a.setResult(RESULT_OK, null);
            LocalMemory.getInstance().getManager().populateKeys(context);
            SystemClock.sleep(500);
            Intent intent = new Intent(context, MainProfileActivity.class);
            context.startActivity(intent);
            a.finish();
        }
    }

    @Override
    public void RemoveKeyTaskComplete(String result, String key, Context context) {
        if(result.equals("401")){
            Toast.makeText(context, "Cannot remove the key pair.", Toast.LENGTH_LONG).show();
        } else {
            LocalMemory.getInstance().removeKey(key);
            Intent intent = new Intent(context, MainProfileActivity.class);
            if(LocalMemory.getInstance().getStartAct()) {
                SystemClock.sleep(500);
                context.startActivity(intent);
            }
            Activity a = (Activity) context;
            a.setResult(RESULT_OK, null);
            a.finish();
        }
    }

    @Override
    public void RemoveMessageTaskComplete(String result, int id, Context context) {
        if(result.equals("401")){
            Toast.makeText(context, "Cannot remove the message.", Toast.LENGTH_LONG).show();
        } else {
            LocalMemory.getInstance().removeMessage(id);

            //LocalMemory.getInstance().getManager().populateMessages(context);
        }
    }

    @Override
    public void RemoveLocationTaskComplete(String result, String name, Context context) {
        if(result.equals("401")){
            Toast.makeText(context, "Cannot remove the location.", Toast.LENGTH_LONG).show();
        } else {
            LocalMemory.getInstance().removeLocation(name);
            Intent intent = new Intent(context, MainLocationsActivity.class);
            context.startActivity(intent);

            Activity a = (Activity) context;
            a.finish();
        }
    }


    @Override
    public void SendMessageTaskComplete(String result, Context context) {
        if(result.equals("401")){
            Toast.makeText(context, "Cannot send message.", Toast.LENGTH_LONG).show();
        } else {
            Activity a = (Activity) context;
                a.setResult(RESULT_OK, null);
                a.finish();
            LocalMemory.getInstance().getManager().populateMessages(context);
        }
    }

}
