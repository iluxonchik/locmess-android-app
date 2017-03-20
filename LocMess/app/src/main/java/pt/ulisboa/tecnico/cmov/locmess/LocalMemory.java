package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

import java.util.ArrayList;
import java.util.List;


public class LocalMemory {

    private static LocalMemory instance;
    private List<String> keys = new ArrayList<>();

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



    public static synchronized LocalMemory getInstance(){
        if(instance==null)
            instance = new LocalMemory();
        return instance;
    }
}
