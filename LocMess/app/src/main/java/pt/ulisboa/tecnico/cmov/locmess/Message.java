package pt.ulisboa.tecnico.cmov.locmess;

import java.util.List;

/**
 * Created by Valentyn on 22-03-2017.
 */

public class Message {
    private int id;
    private String title;
    private String autor;
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
        autor=aut;
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

    public String getAutor(){
        return autor;
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


}
