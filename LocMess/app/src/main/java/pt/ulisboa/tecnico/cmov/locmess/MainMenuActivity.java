package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void profile(View v) {
        Intent intent = new Intent(this, MainProfileActivity.class);
        startActivity(intent);
    }

    public void messages(View v) {

    }

    public void locations(View v) {

    }
}
