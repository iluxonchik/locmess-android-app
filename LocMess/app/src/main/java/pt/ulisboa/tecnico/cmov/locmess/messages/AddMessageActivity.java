package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.locations.Location;

public class AddMessageActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        context=this;

        AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.editTextMessageLocation);

        List<String> locations = new ArrayList<>();
        for (Location l: LocalMemory.getInstance().getLocations()){
            locations.add(l.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations.toArray(new String[0]));
        location.setAdapter(adapter);

        Intent intent = getIntent();
        String loc = intent.getStringExtra("LOCATION");
        location.setText(loc);
    }

    public void nextMessageAct(View v){
        Intent myIntent = new Intent(context, AddMessageSecondActivity.class);

        EditText title = (EditText) findViewById(R.id.editTextMessageTitle);
        AutoCompleteTextView location = (AutoCompleteTextView) findViewById(R.id.editTextMessageLocation);
        EditText text = (EditText) findViewById(R.id.editTextMessageText);

        myIntent.putExtra("TITLE", title.getText().toString());
        myIntent.putExtra("LOCATION", location.getText().toString());
        myIntent.putExtra("TEXT", text.getText().toString());

        ((Activity)context).startActivityForResult(myIntent,20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 20) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, null);
                this.finish();

            }
        }
    }



}
