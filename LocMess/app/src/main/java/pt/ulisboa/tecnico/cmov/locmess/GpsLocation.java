package pt.ulisboa.tecnico.cmov.locmess;

/**
 * Created by Valentyn on 20-03-2017.
 */

public class GpsLocation extends Location {
    private long latitude;
    private long longitude;
    private long radious;

    public GpsLocation(String name,long lat,long longi,long rad){
        super(name);
        latitude=lat;
        longitude=longi;
        radious=rad;
    }

    public long getLatitude(){
        return latitude;
    }

    public long getLongitude(){
        return longitude;
    }

    public long getRadious(){
        return radious;
    }
}
