package pt.ulisboa.tecnico.cmov.locmess.locations;

import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.locations.Location;

/**
 * Created by Valentyn on 20-03-2017.
 */

public class WifiLocation extends Location {
    private List<String> points;

    public WifiLocation(String name, List<String> poi){
        super(name);
        points=poi;
    }

    public List<String> getPoints(){
        return points;
    }
}
