package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 22-03-2017.
 */

public class MyDate {
    private int day;
    private int month;
    private int year;

    public MyDate(int d,int m,int y){
        day=d;
        month=m;
        year=y;
    }

    public int getDay(){
        return day;
    }

    public int getMonth(){
        return month;
    }

    public int getYear(){
        return year;
    }
}
