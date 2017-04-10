package pt.ulisboa.tecnico.cmov.locmess.locations;

/**
 * Created by Valentyn on 20-03-2017.
 */

public class GpsLocation extends Location {
    private double latitude;
    private double longitude;
    private double radious;

    public GpsLocation(String name,double lat,double longi,double rad){
        super(name);
        latitude=lat;
        longitude=longi;
        radious=rad;
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getRadious(){
        return radious;
    }
}
