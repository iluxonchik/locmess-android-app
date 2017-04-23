package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void profile(View v) {
        LocalMemory.getInstance().getManager().populateKeys(this);
    }

    public void messages(View v) {
        Intent intent = new Intent(this, MainMessagesActivity.class);
        startActivity(intent);
    }

    public void locations(View v) {
        Intent intent = new Intent(this, MainLocationsActivity.class);
        startActivity(intent);
    }

    public void logout(View v) {
        LocalMemory.getInstance().getManager().logout(this);
    }
}
