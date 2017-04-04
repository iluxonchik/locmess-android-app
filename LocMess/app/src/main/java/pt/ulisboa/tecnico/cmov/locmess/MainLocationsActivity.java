package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainLocationsActivity extends AppCompatActivity {

    private List<String> locations = new ArrayList<>();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_locations);

        context=this;

        List<Location> loc = LocalMemory.getInstance().getLocations();
        for(int i=0;i<loc.size();i++)
            locations.add(loc.get(i).getName());

        populateListView();
    }

    private void populateListView() {
        MyLocationsAdapter adapter = new MyLocationsAdapter(locations,this);
        ListView list = (ListView) findViewById(R.id.listViewLocations);
        list.setAdapter(adapter);
    }

    public void addLocation(View v) {
        Intent intent = new Intent(this, AddLocationActivity.class);
        finish();
        startActivity(intent);
    }

}
