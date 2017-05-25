package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.SimWifiP2pManager.Channel;
import pt.ulisboa.tecnico.cmov.locmess.locations.Location;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;
import pt.ulisboa.tecnico.cmov.locmess.utils.InternalStorage;


public class LocalMemory {

    private static final String LOG_TAG = LocalMemory.class.getName();
    private static final String NYAM_FILE_NAME = "nyam.data";
    private static final String AM_FILE_NAME = "am.data";
    private static Context context;
    private static LocalMemory instance;
    private Manager manager = new Manager();
    private List<String> keys = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
    private List<Message> decentralizedMessages = new ArrayList<>();
    private List<Message> decentralizedMessagesToSend = new ArrayList<>();

    private HashSet<Message> notYetAcceptedMessages = new HashSet<>();
    private HashMap<String, Message> acceptedMessages = new HashMap<>();

    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private boolean mBound = false;
    private boolean refreshMessagesScreen = true;

    private String loggedUserMail = "";
    private String loggedUserPassword = "";
    private String sessionKey = "";

    private boolean startAct = false;

    private int idCounter = -1;

    private boolean unique = false;

    public LocalMemory() {

    }

    public void addNotYetAcceptedMessages(HashSet<Message> newMsgs) {
        String id = null;
        for (Message msg : newMsgs) {
            id = new Integer(msg.getId()).toString();
            if (!acceptedMessages.containsKey(id)) {
                // only add the message if it's not already been accepted
                notYetAcceptedMessages.add(msg);
            }
        }

        try {
            InternalStorage.writeToDisk(context, NYAM_FILE_NAME, notYetAcceptedMessages);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not save to disk!");
        }
    }

    public void acceptMessage(Message m) {
        notYetAcceptedMessages.remove(m);
        acceptedMessages.put(new Integer(m.getId()).toString(), m);

        try {
            InternalStorage.writeToDisk(context, NYAM_FILE_NAME, notYetAcceptedMessages);
            InternalStorage.writeToDisk(context, AM_FILE_NAME, acceptedMessages);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Could not save to disk!");
        }
    }

    public void setAcceptedMessages(HashMap<String, Message> msgs) {
        acceptedMessages = msgs;
    }

    public void setNotYetAcceptedMessages(HashSet<Message> msgs) {
        notYetAcceptedMessages = msgs;
    }

    public boolean getRefreshMessagesScreen() {
        return refreshMessagesScreen;
    }

    public void setRefreshMessagesScreen(boolean r) {
        refreshMessagesScreen = r;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void decrementId() {
        idCounter = idCounter - 1;
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

    public void setStartAct(boolean b) {
        startAct = b;
    }

    public boolean getStartAct() {
        return startAct;
    }

    public void setLoggedUserMail(String m) {
        loggedUserMail = m;
    }

    public String getLoggedUserMail() {
        return loggedUserMail;
    }

    public void setLoggedUserPass(String p) {
        loggedUserPassword = p;
    }

    public String getLoggedUserPass() {
        return loggedUserPassword;
    }

    public boolean isUnique(){
        return unique;
    }

    public void setUnique(){
        this.unique = true;
    }


    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public List<String> getKeys() {
        return keys;
    }


    public void addKey(String k) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(k))
                return;
        }
        keys.add(k);
    }

    public void loadKeys(List<String> l) {
        keys = new ArrayList<>();
        for (String k : l)
            keys.add(k);
    }

    public void removeKey(String key) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(key))
                keys.remove(i);
        }
    }

    public void replaceKey(String oldKey, String newKey) {
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(oldKey))
                keys.set(i, newKey);
        }
    }

    public Message getMessage(int id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id)
                return messages.get(i);
        }
        return null;
    }

    public Message getDecentralizedMessage(int id) {
        for (int i = 0; i < decentralizedMessages.size(); i++) {
            if (decentralizedMessages.get(i).getId() == id)
                return decentralizedMessages.get(i);
        }
        return null;
    }

    public Message getDecentralizedMessageToSend(int id) {
        for (int i = 0; i < decentralizedMessagesToSend.size(); i++) {
            if (decentralizedMessagesToSend.get(i).getId() == id)
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

    public List<Message> getDecentralizedmessagesToSend() {
        return decentralizedMessagesToSend;
    }


    public void addMessage(Message m) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == m.getId())
                return;
        }
        messages.add(m);
    }

    public void addDecentralizedMessage(Message m) {
        for (int i = 0; i < decentralizedMessages.size(); i++) {
            if (decentralizedMessages.get(i).getId() == m.getId())
                return;
        }
        decentralizedMessages.add(m);
    }

    public void addDecentralizedMessageToSend(Message m) {
        decentralizedMessagesToSend.add(m);
    }

    public void loadMessages(List<Message> l) {
        messages = l;
    }


    public void removeMessage(int id) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == id)
                messages.remove(i);
        }
    }

    public void removeDescentralizedMessage(int id) {
        for (int i = 0; i < decentralizedMessages.size(); i++) {
            if (decentralizedMessages.get(i).getId() == id) {
                decentralizedMessages.remove(i);
            }
        }
    }

    public void removeDescentralizedMessageToSend(int id) {
        for (int i = 0; i < decentralizedMessagesToSend.size(); i++) {
            if (decentralizedMessagesToSend.get(i).getId() == id)
                decentralizedMessagesToSend.remove(i);
        }
    }


    public Location getLocation(String name) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getName().equals(name))
                return locations.get(i);
        }
        return null;
    }

    public List<Location> getLocations() {
        return locations;
    }


    public void addLocation(Location l) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getName().equals(l.getName()))
                return;
        }
        locations.add(l);
    }

    public void loadLocations(List<Location> l) {
        locations = l;
    }


    public void removeLocation(String name) {
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getName().equals(name))
                locations.remove(i);
        }
    }

    public Manager getManager() {
        return manager;
    }


    public static synchronized LocalMemory getInstance() {
        if (instance == null) {
            instance = new LocalMemory();
            if (InternalStorage.fileExists(instance.getContext(), NYAM_FILE_NAME)) {
                try {
                    Object loaded = InternalStorage.readFromDisk(instance.context, NYAM_FILE_NAME);
                    instance.setNotYetAcceptedMessages((HashSet<Message>) loaded);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (InternalStorage.fileExists(instance.getContext(), AM_FILE_NAME)) {
                try {
                    Object loaded = InternalStorage.readFromDisk(instance.context, AM_FILE_NAME);
                    instance.setAcceptedMessages((HashMap<String, Message>) loaded);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return instance;
    }

    public HashSet<Message> getNotYetAcceptedMessages() {
        return notYetAcceptedMessages;
    }


    public Message getAcceptedMessage(String id) {
        return acceptedMessages.get(id);
    }

    public boolean isNotYetAcceptedMessagesAvailable() {
        return notYetAcceptedMessages.size() > 0;
    }

    public List<Message> getAcceptedMessages() {
        if (acceptedMessages.isEmpty()) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(acceptedMessages.values());
        }
    }

    public static void setContext(Context newContext) {
        context = newContext;
    }

    public static Context getContext() {
        return context;
    }
}
