package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

import java.util.ArrayList;
import java.util.List;


public class LocalMemory {

    private static LocalMemory instance;
    private List<String> keys = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();

    private String loggedUserMail;
    private String loggedUserPassword;
    private String sessionKey;

    public LocalMemory(){

    }

    public void setLoggedUserMail(String m){
        loggedUserMail=m;
    }

    public String getLoggedUserMail(){
        return loggedUserMail;
    }

    public void setLoggedUserPass(String p){
        loggedUserPassword=p;
    }

    public String getLoggedUserPass(){
        return loggedUserPassword;
    }


    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey)
    {
        this.sessionKey = sessionKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void addKey(String k){
        for(int i=0;i<keys.size();i++){
            if(keys.get(i).equals(k))
                return;
        }
        keys.add(k);
    }

    public void loadKeys(List<String> l){
        keys=l;
    }


    public void removeKey(String key){
        for(int i=0;i<keys.size();i++){
            if(keys.get(i).equals(key))
                keys.remove(i);
        }
    }

    public void replaceKey(String oldKey,String newKey){
        for(int i=0;i<keys.size();i++){
            if(keys.get(i).equals(oldKey))
                keys.set(i,newKey);
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location l){
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).getName().equals(l.getName()))
                return;
        }
        locations.add(l);
    }

    public void loadLocations(List<Location> l){
        locations=l;
    }


    public void removeLocation(String name){
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).equals(name))
                locations.remove(i);
        }
    }



    public static synchronized LocalMemory getInstance(){
        if(instance==null)
            instance = new LocalMemory();
        return instance;
    }
}
