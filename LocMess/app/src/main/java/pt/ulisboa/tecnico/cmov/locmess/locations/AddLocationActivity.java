package pt.ulisboa.tecnico.cmov.locmess.locations;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.R;

public class AddLocationActivity extends AppCompatActivity{

    private Context context;
    private List<String> foundDevices = new ArrayList<String>();
    private GetGpsLocation getGpsLocation;

    //Wifi variables
    WifiManager wifi;
    List<ScanResult> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        context = this;

        getGpsLocation = new GetGpsLocation(context);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerType);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String type = adapterView.getSelectedItem().toString();

                EditText latitudeE = (EditText) findViewById(R.id.editTextLatitude);
                EditText longitudeE = (EditText) findViewById(R.id.editTextLongitude);
                EditText radiousE = (EditText) findViewById(R.id.editTextRadious);
                TextView tv12 = (TextView) findViewById(R.id.textView12);
                TextView tv13 = (TextView) findViewById(R.id.textView13);
                TextView tv14 = (TextView) findViewById(R.id.textView14);
                TextView tv19 = (TextView) findViewById(R.id.textView19);
                CheckBox checkbox = (CheckBox) findViewById(R.id.checkboxUseCurrentLocation);
                ListView listV = (ListView) findViewById(R.id.listViewFoundDevices);


                if (type.equals("GPS")) {
                    latitudeE.setVisibility(View.VISIBLE);
                    longitudeE.setVisibility(View.VISIBLE);
                    radiousE.setVisibility(View.VISIBLE);
                    tv12.setVisibility(View.VISIBLE);
                    tv13.setVisibility(View.VISIBLE);
                    tv14.setVisibility(View.VISIBLE);
                    checkbox.setVisibility(View.VISIBLE);
                    tv19.setVisibility(View.INVISIBLE);
                    listV.setVisibility(View.INVISIBLE);

                } else {
                    wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (wifi.isWifiEnabled() == false) {
                        Toast.makeText(getApplicationContext(), "Wifi is disabled... Making it enabled.",
                                Toast.LENGTH_LONG).show();
                        wifi.setWifiEnabled(true);
                    }

                    List<ScanResult>results = wifi.getScanResults();

                    for (ScanResult r : results ) {
                        foundDevices.add(r.SSID);
                    }

                    latitudeE.setVisibility(View.INVISIBLE);
                    longitudeE.setVisibility(View.INVISIBLE);
                    radiousE.setVisibility(View.INVISIBLE);
                    tv12.setVisibility(View.INVISIBLE);
                    tv13.setVisibility(View.INVISIBLE);
                    tv14.setVisibility(View.INVISIBLE);
                    checkbox.setVisibility(View.INVISIBLE);
                    tv19.setVisibility(View.VISIBLE);
                    listV.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, foundDevices);
                    listV.setAdapter(arrayAdapter);

                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(context, MainLocationsActivity.class);
            finish();
            context.startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void addLocation(View v) {
        String name = ((EditText) findViewById(R.id.editTextLocationName)).getText().toString();
        String latitude = ((EditText) findViewById(R.id.editTextLatitude)).getText().toString();
        String longitude = ((EditText) findViewById(R.id.editTextLongitude)).getText().toString();
        String radious = ((EditText) findViewById(R.id.editTextRadious)).getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerType);

        Manager m = LocalMemory.getInstance().getManager();

        if (spinner.getSelectedItem().toString().equals("GPS")) {
            if(m.addGpsLocation(context, name, latitude, longitude, radious)) {
                Intent intent = new Intent(context, MainLocationsActivity.class);
                finish();
                context.startActivity(intent);
            }
        }
        else {
            m.addWifiLocation(context, name, foundDevices);
        }

    }

    public void useCurrentLocation(View v) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkboxUseCurrentLocation);
        if(!checkBox.isChecked()) return;

        double rad = 1;

        EditText latitudeE = (EditText) findViewById(R.id.editTextLatitude);
        EditText longitudeE = (EditText) findViewById(R.id.editTextLongitude);
        EditText radiousE = (EditText) findViewById(R.id.editTextRadious);

        latitudeE.setText("" + getGpsLocation.getLatitude());
        longitudeE.setText("" + getGpsLocation.getLongitude());
        radiousE.setText("" + rad);
    }

}
