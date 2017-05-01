package pt.ulisboa.tecnico.cmov.locmess.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.messages.MainMessagesActivity;
import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;

public class MainMenuActivity extends AppCompatActivity {

    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        context=this;

        LocalMemory.getInstance().getManager().populateLocations(this);

    }

    public void profile(View v) {
        LocalMemory.getInstance().getManager().populateKeys(this);
    }

    public void messages(View v) {
        LocalMemory.getInstance().getManager().populateMessages(this);
    }

    public void locations(View v) {
        Intent intent = new Intent(this, MainLocationsActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
        LocalMemory.getInstance().getManager().logout(this);
    }
}
