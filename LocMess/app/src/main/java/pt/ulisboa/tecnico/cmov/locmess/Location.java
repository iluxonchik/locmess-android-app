package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

public abstract class Location {
    private String name;

    public Location(String n){
        name=n;
    }

    public String getName(){
        return name;
    }
}
