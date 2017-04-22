package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class ViewKeyActivity extends AppCompatActivity {

    private Context context;
    private String key="";
    private String value="";
    private String oldKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_key);

        context=this;

        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");
        value = intent.getStringExtra("VALUE");
        if(key!=null && value!=null )
            oldKey = key+":"+value;

        EditText keyE = (EditText) findViewById(R.id.editTextKey);
        EditText valueE = (EditText) findViewById(R.id.editTextValue);

        if(key!=null)
            keyE.setText(key);
        if(value!=null)
            valueE.setText(value);

    }

    public void saveKey(View v) {

        EditText keyE = (EditText) findViewById(R.id.editTextKey);
        EditText valueE = (EditText) findViewById(R.id.editTextValue);

        Manager m = LocalMemory.getInstance().getManager();

        m.updateKey(context,keyE.getText().toString(),valueE.getText().toString());

    }
}
