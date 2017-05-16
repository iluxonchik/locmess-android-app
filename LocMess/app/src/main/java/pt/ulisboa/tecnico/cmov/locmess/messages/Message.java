package pt.ulisboa.tecnico.cmov.locmess.messages;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Message {
    private int id;
    private String title;
    private String author;
    private String location;
    private String text;
    private boolean isCentralized;
    private boolean isBlackList;
    private List<String> keys;
    private String startDate;
    private String endDate;


    public Message(int id, String tit, String aut, String loc, String txt, boolean cent, boolean black, List<String> ks,String sDate,String eDate){
        this.id=id;
        title=tit;
        author =aut;
        location=loc;
        text=txt;
        isCentralized=cent;
        isBlackList=black;
        keys=ks;
        startDate=sDate;
        endDate=eDate;
    }

    public int getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public String getLocation(){
        return location;
    }

    public String getText(){
        return text;
    }

    public boolean isCentralized(){
        return isCentralized;
    }

    public boolean isBlackList(){
        return isBlackList;
    }

    public List<String> getKeys(){
        return keys;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }
    
    public JSONObject getJsonObject(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("id", id);
            jo.put("title", title);
            jo.put("author", author);
            jo.put("location", location);
            jo.put("text", text);
            jo.put("isCentralized", isCentralized);
            jo.put("isBlackList", isBlackList);
            jo.put("keys", keys.toString());
            jo.put("sDate", startDate);
            jo.put("eDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Message) {
            Message other = (Message) obj;
            return this.getId() == other.getId();
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * Creates a new Message instance from a JSON Object.
     * @param jsonObj - JSON object representing the Message
     * @return Message instance
     * @throws JSONException
     */
    public static Message fromJSONObject(JSONObject jsonObj) throws JSONException {
        List<String> prop = new ArrayList<>();

        JSONObject propJS = jsonObj.getJSONObject("properties");
        Iterator<String> keys = propJS.keys();

        while(keys.hasNext() ) {
            String key = keys.next();
            prop.add(key+":"+propJS.get(key));
        }

        return new Message(
                Integer.parseInt(jsonObj.getString("msg_id")),
                jsonObj.getString("title"),
                jsonObj.getString("author"),
                jsonObj.getString("location"),
                jsonObj.getString("text"),
                Boolean.valueOf(jsonObj.getString("is_centralized")),
                Boolean.valueOf(jsonObj.getString("is_black_list")),
                prop,
                jsonObj.getString("valid_from"),
                jsonObj.getString("valid_until")
        );
    }
}
