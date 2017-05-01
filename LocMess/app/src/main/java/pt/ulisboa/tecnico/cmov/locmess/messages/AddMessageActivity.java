package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import pt.ulisboa.tecnico.cmov.locmess.R;

public class AddMessageActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        context=this;
    }

    public void nextMessageAct(View v){
        Intent myIntent = new Intent(context, AddMessageSecondActivity.class);

        EditText title = (EditText) findViewById(R.id.editTextMessageTitle);
        EditText location = (EditText) findViewById(R.id.editTextMessageLocation);
        EditText text = (EditText) findViewById(R.id.editTextMessageText);

        myIntent.putExtra("TITLE", title.getText().toString());
        myIntent.putExtra("LOCATION", location.getText().toString());
        myIntent.putExtra("TEXT", text.getText().toString());

        context.startActivity(myIntent);
    }



}
