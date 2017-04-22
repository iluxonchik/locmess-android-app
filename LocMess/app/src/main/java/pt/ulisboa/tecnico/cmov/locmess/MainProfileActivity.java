package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainProfileActivity extends AppCompatActivity {

    private List<String> keys = new ArrayList<>();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        context=this;

        LocalMemory.getInstance().getManager().populateUserKeys(context);
        keys = LocalMemory.getInstance().getUserKeys();

        populateListView();

    }

    private void populateListView() {
        MyProfileAdapter adapter = new MyProfileAdapter(keys,this);
        ListView list = (ListView) findViewById(R.id.listViewProfile);
        list.setAdapter(adapter);
    }

    public void addKey(View v) {
        Intent intent = new Intent(this, ViewKeyActivity.class);
        startActivity(intent);
    }

}
