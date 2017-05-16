package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.ulisboa.tecnico.cmov.locmess.locations.Location;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;


public class LocalMemory {

    private static LocalMemory instance;
    private Manager manager = new Manager();
    private List<String> keys = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Message> decentralizedMessages = new ArrayList<>();
    private List<Message> decentralizedMessagesToSend = new ArrayList<>();

    private HashSet<Message> notYetAcceptedMessages = new HashSet<>();
    private HashSet<Message> acceptedMessages = new HashSet<>();

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private boolean refreshMessagesScreen=true;

    private String loggedUserMail="";
    private String loggedUserPassword="";
    private String sessionKey="";

    private boolean startAct = false;

    private int idCounter = -1;

    public LocalMemory(){

    }

    public boolean getRefreshMessagesScreen(){
        return refreshMessagesScreen;
    }

    public void setRefreshMessagesScreen(boolean r){
        refreshMessagesScreen=r;
    }

    public int getIdCounter(){
        return idCounter;
    }

    public void decrementId(){
        idCounter = idCounter -1;
    }

    public void setmManager(SimWifiP2pManager mManager) {
        this.mManager = mManager;
    }

    public void setmChannel(Channel mChannel) {
        this.mChannel = mChannel;
    }

    public SimWifiP2pManager getmManager() {
        return mManager;
    }

    public Channel getmChannel() {
        return mChannel;
    }

    public boolean ismBound() {
        return mBound;
    }

    public void setmBound(boolean mBound) {
        this.mBound = mBound;
    }

    public void setStartAct(boolean b){
        startAct=b;
    }

    public boolean getStartAct(){
        return startAct;
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
        keys = new ArrayList<>();
        for (String k : l)
            keys.add(k);
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

    public Message getMessage(int id) {
        for(int i=0;i<messages.size();i++){
            if(messages.get(i).getId()==id)
                return messages.get(i);
        }
        return null;
    }

    public Message getDecentralizedMessage(int id) {
        for(int i=0;i<decentralizedMessages.size();i++){
            if(decentralizedMessages.get(i).getId()==id)
                return decentralizedMessages.get(i);
        }
        return null;
    }

    public Message getDecentralizedMessageToSend(int id) {
        for(int i=0;i<decentralizedMessagesToSend.size();i++){
            if(decentralizedMessagesToSend.get(i).getId()==id)
                return decentralizedMessagesToSend.get(i);
        }
        return null;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getDecentralizedMessages() {
        return decentralizedMessages;
    }

    public List<Message>  getDecentralizedmessagesToSend() {
        return decentralizedMessagesToSend;
    }


    public void addMessage(Message m){
        for(int i=0;i<messages.size();i++){
            if(messages.get(i).getId()==m.getId())
                return;
        }
        messages.add(m);
    }

    public void addDecentralizedMessage(Message m) {
        for(int i=0;i<decentralizedMessages.size();i++){
            if(decentralizedMessages.get(i).getId()==m.getId())
                return;
        }
        decentralizedMessages.add(m);
    }

    public void addDecentralizedMessageToSend(Message m) { decentralizedMessagesToSend.add(m);}

    public void loadMessages(List<Message> l){
        messages=l;
    }


    public void removeMessage(int id){
        for(int i=0;i<messages.size();i++){
            if(messages.get(i).getId()==id)
                messages.remove(i);
        }
    }

    public void removeDescentralizedMessage(int id){
        for(int i=0;i<decentralizedMessages.size();i++){
            if (decentralizedMessages.get(i).getId()==id){
            decentralizedMessages.remove(i);
            }
        }
    }
    public void removeDescentralizedMessageToSend(int id){
        for(int i=0;i<decentralizedMessagesToSend.size();i++){
            if (decentralizedMessagesToSend.get(i).getId()==id)
                decentralizedMessagesToSend.remove(i);
        }
    }


    public Location getLocation(String name) {
        for(int i=0;i<locations.size();i++){
            if(locations.get(i).getName().equals(name))
                return locations.get(i);
        }
        return null;
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
            if(locations.get(i).getName().equals(name))
                locations.remove(i);
        }
    }

    public Manager getManager(){
        return manager;
    }


    public static synchronized LocalMemory getInstance(){
        if(instance == null) {
            instance = new LocalMemory();
        }
        return instance;
    }

    public HashSet<Message> getNotYetAcceptedMessages() {
        return notYetAcceptedMessages;
    }

    public HashSet<Message> getAcceptedMessages() {
        return acceptedMessages;
    }
}
