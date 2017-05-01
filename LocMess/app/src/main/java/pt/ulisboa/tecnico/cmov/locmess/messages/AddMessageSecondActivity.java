package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream:LocMess/app/src/main/java/pt/ulisboa/tecnico/cmov/locmess/AddMessageSecondActivity.java
import static pt.ulisboa.tecnico.cmov.locmess.R.id.startTimePicker;
=======
import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.MyDate;
import pt.ulisboa.tecnico.cmov.locmess.R;

import static pt.ulisboa.tecnico.cmov.locmess.R.id.datePicker;
>>>>>>> Stashed changes:LocMess/app/src/main/java/pt/ulisboa/tecnico/cmov/locmess/messages/AddMessageSecondActivity.java

public class AddMessageSecondActivity extends AppCompatActivity {

    private Context context;
    private String title;
    private String location;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message_second);

        context=this;

        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        location = intent.getStringExtra("LOCATION");
        text = intent.getStringExtra("TEXT");

        TimePicker tp = (TimePicker) findViewById(startTimePicker);
        tp.setEnabled(false);

        RelativeLayout linear=(RelativeLayout) findViewById(R.id.layoutProfilePreferences);

        List<String> keys = LocalMemory.getInstance().getKeys();
        for(int i=0;i<keys.size();i++){
            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(keys.get(i));
            //Alinhar as chekboxes para não estarem sobrepostas
            linear.addView(checkBox);
        }

    }

    public void useStartTimeLimit(View v) {
        CheckBox ck = (CheckBox) findViewById(R.id.checkboxUseStartTimeLimit);
        TimePicker tp = (TimePicker) findViewById(startTimePicker);
        if(ck.isChecked())
            tp.setEnabled(true);
        else
            tp.setEnabled(false);

    }

    public void sendMessage(View v) {
        boolean centralized=true;
        boolean black_list=true;
        Spinner delivery = (Spinner) findViewById(R.id.spinnerDeliveryMode);
        Spinner policy = (Spinner) findViewById(R.id.spinnerPolicyType);
        TimePicker dp = (TimePicker) findViewById(startTimePicker);

        if(!delivery.getSelectedItem().equals("Centralized"))
            centralized=false;
        if(!policy.getSelectedItem().equals(("Black list")))
            black_list=false;

        List<String> keys = new ArrayList<>();

        String sDate = "";
        String eDate = "";

        CheckBox ck = (CheckBox) findViewById(R.id.checkboxUseStartTimeLimit);
        if(ck.isChecked())
            sDate= "xx";

        ViewGroup vg = (ViewGroup)v.getParent();
        for(int i=0;i<vg.getChildCount();i++) {
            View vchi = vg.getChildAt(i);

            if (vchi instanceof CheckBox) {
                if(((CheckBox) vchi).isChecked() && !((CheckBox) vchi).getText().toString().equals("Use Time Limit") )
                    keys.add(((CheckBox) vchi).getText().toString());

            }
        }

        Manager m = LocalMemory.getInstance().getManager();
        m.sendMessage(context,title,location,text,centralized,black_list,keys,sDate,eDate);

    }

}
