package pt.ulisboa.tecnico.cmov.locmess.locations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.locmess.AddMessageActivity;
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.locations.GpsLocation;
import pt.ulisboa.tecnico.cmov.locmess.locations.Location;
import pt.ulisboa.tecnico.cmov.locmess.locations.WifiLocation;

public class ViewLocationActivity extends AppCompatActivity {

    private String location;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);

        context=this;

        Intent intent = getIntent();
        location = intent.getStringExtra("LOCATION");

        TextView lName = (TextView) findViewById(R.id.textViewLName);
        TextView lLat = (TextView) findViewById(R.id.textViewLLatitude);
        TextView lLon = (TextView) findViewById(R.id.textViewLLongitude);
        TextView lRad = (TextView) findViewById(R.id.textViewLRadious);

        TextView tv16 = (TextView) findViewById(R.id.textView16);
        TextView tv17 = (TextView) findViewById(R.id.textView17);
        TextView tv18 = (TextView) findViewById(R.id.textView18);
        TextView tv20 = (TextView) findViewById(R.id.textView20);

        ListView lv = (ListView) findViewById(R.id.listViewListDevices);


        Location l = LocalMemory.getInstance().getLocation(location);

        if (l instanceof GpsLocation){
            lLat.setVisibility(View.VISIBLE);
            lLon.setVisibility(View.VISIBLE);
            lRad.setVisibility(View.VISIBLE);
            tv16.setVisibility(View.VISIBLE);
            tv17.setVisibility(View.VISIBLE);
            tv18.setVisibility(View.VISIBLE);
            tv20.setVisibility(View.INVISIBLE);
            lv.setVisibility(View.INVISIBLE);

            GpsLocation gpsL = (GpsLocation) l;
            lName.setText(gpsL.getName());
            lLat.setText(""+gpsL.getLatitude());
            lLon.setText(""+gpsL.getLongitude());
            lRad.setText(""+gpsL.getRadious());
        }
        else if (l instanceof WifiLocation){
            lLat.setVisibility(View.INVISIBLE);
            lLon.setVisibility(View.INVISIBLE);
            lRad.setVisibility(View.INVISIBLE);
            tv16.setVisibility(View.INVISIBLE);
            tv17.setVisibility(View.INVISIBLE);
            tv18.setVisibility(View.INVISIBLE);
            tv20.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);

            WifiLocation wifiL = (WifiLocation) l;
            lName.setText(wifiL.getName());

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,wifiL.getPoints());
            lv.setAdapter(arrayAdapter);

        }
    }

    public void postMessage(View v) {
        Intent intent = new Intent(this, AddMessageActivity.class);
        intent.putExtra("LOCATION", location);
        startActivity(intent);
    }
}
