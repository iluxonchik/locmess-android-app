package pt.ulisboa.tecnico.cmov.locmess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.serverConnections.RegisterTask;
import pt.ulisboa.tecnico.cmov.locmess.serverConnections.TaskDelegate;

/**
 * Created by Valentyn on 23-03-2017.
 */

public class Manager implements TaskDelegate{

    //server variables
    //rogressDialog pd;
    ProgressDialog progress;
    public Manager() {
    }

    public void login(Context context , String mail, String pass){
        LocalMemory.getInstance().setLoggedUserMail(mail);
        LocalMemory.getInstance().setLoggedUserPass(pass);
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
        LocalMemory.getInstance().addLocation(new GpsLocation(name,Double.parseDouble(latitude),Double.parseDouble(longitude),Double.parseDouble(radius)));
        return true;
        // Send to the server a new locaiton
    }

    public void addWifiLocation(Context context , String name, List<String> foundDevices){
        LocalMemory.getInstance().addLocation(new WifiLocation(name,foundDevices));
        Intent intent = new Intent(context, MainLocationsActivity.class);
        context.startActivity(intent);
    }

    public void sendMessage(Context context,int id,String title,String autor,String location,String text,boolean isCentralized,boolean isBlackList, List<String> keys, MyDate date){
        Message msg = new Message(0,title,autor,location,text,isCentralized,isBlackList,keys,date);

        LocalMemory.getInstance().addMessage(msg);
        Intent intent = new Intent(context, MainMessagesActivity.class);
        context.startActivity(intent);
    }

    public void addKey(Context context , String keyPair){
        LocalMemory.getInstance().addKey(keyPair);
        Intent intent = new Intent(context, MainProfileActivity.class);
        context.startActivity(intent);
    }

    public void editKey(Context context , String oldKey, String keyPair){
        LocalMemory.getInstance().replaceKey(oldKey, keyPair);
        Intent intent = new Intent(context, MainProfileActivity.class);
        context.startActivity(intent);
    }

    public void removeLocation(Context context , String name){
        LocalMemory.getInstance().removeLocation(name);
        Intent myIntent = new Intent(context, MainLocationsActivity.class);
        context.startActivity(myIntent);
        Activity a = (Activity) context;
        a.finish();
    }

    public void removeMessage(Context context , int id){

        LocalMemory.getInstance().removeMessage(id);
        Intent myIntent = new Intent(context, MainMessagesActivity.class);
        context.startActivity(myIntent);
        Activity a = (Activity) context;
        a.finish();

    }

    public void removeKey(Context context , String key){

        LocalMemory.getInstance().removeKey(key);
        Intent myIntent = new Intent(context, MainProfileActivity.class);
        context.startActivity(myIntent);
        Activity a = (Activity) context;
        a.finish();
    }


    @Override
    public void TaskCompletionResult(String result, Context context) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        context.startActivity(intent);
    }
}
