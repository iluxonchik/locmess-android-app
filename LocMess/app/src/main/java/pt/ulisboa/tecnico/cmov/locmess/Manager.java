package pt.ulisboa.tecnico.cmov.locmess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Valentyn on 23-03-2017.
 */

public class Manager {
    public void login(Context context ,String mail,String pass){

        Intent intent = new Intent(context, MainMenuActivity.class);
        context.startActivity(intent);

        LocalMemory.getInstance().setLoggedUserMail(mail);
        LocalMemory.getInstance().setLoggedUserPass(pass);
    }

    public void register(Context context, String mail, String pass, String confirm_pass){
        if(mail.length()<5) {
            Toast.makeText(context, "The mail must have at least 5 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(pass.length()<6) {
            Toast.makeText(context, "The password must have at least 6 characters.", Toast.LENGTH_LONG).show();
            return;
        }
        else if(!confirm_pass.equals(pass)) {
            Toast.makeText(context, "The passwords are different.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(context, MainMenuActivity.class);
        context.startActivity(intent);
    }

    public void addGpsLocation(Context context ,String name,String latitude, String longitude, String radious){
        LocalMemory.getInstance().addLocation(new GpsLocation(name,Double.parseDouble(latitude),Double.parseDouble(longitude),Double.parseDouble(radious)));
        Intent intent = new Intent(context, MainLocationsActivity.class);
        context.startActivity(intent);
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


    }
