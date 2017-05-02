package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.R;

import static pt.ulisboa.tecnico.cmov.locmess.R.id.textViewEDate;
import static pt.ulisboa.tecnico.cmov.locmess.R.id.textViewSDate;

//import static pt.ulisboa.tecnico.cmov.locmess.R.id.datePicker;

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

        TextView tvs = (TextView) findViewById(textViewSDate);
        tvs.setEnabled(false);

        TextView tve = (TextView) findViewById(textViewEDate);
        tve.setEnabled(false);

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
        CheckBox cks = (CheckBox) findViewById(R.id.checkboxUseStartTimeLimit);

        DialogFragment newFragment = new TimePickerFragment();

        if(cks.isChecked()) {
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else{
            TextView tvs = (TextView) findViewById(textViewSDate);
            tvs.setEnabled(false);
            tvs.setText("YYYY-MM-DDThh:mm:ss");
        }

    }

    public void useEndTimeLimit(View v) {
        CheckBox cke = (CheckBox) findViewById(R.id.checkboxUseEndTimeLimit);

        DialogFragment newFragment = new TimePickerFragment();
        if(cke.isChecked()) {
            newFragment.show(getFragmentManager(), "timePicker");
        }
        else{
            TextView tve = (TextView) findViewById(textViewEDate);
            tve.setEnabled(false);
            tve.setText("YYYY-MM-DDThh:mm:ss");
        }
    }

    public void sendMessage(View v) {
        boolean centralized=true;
        boolean black_list=true;
        Spinner delivery = (Spinner) findViewById(R.id.spinnerDeliveryMode);
        Spinner policy = (Spinner) findViewById(R.id.spinnerPolicyType);

        if(!delivery.getSelectedItem().equals("Centralized"))
            centralized=false;
        if(!policy.getSelectedItem().equals(("Black list")))
            black_list=false;

        List<String> keys = new ArrayList<>();

        String sDate = "";
        String eDate = "";

        TextView tvs = (TextView) findViewById(R.id.textViewSDate);
        TextView tve = (TextView) findViewById(R.id.textViewEDate);

        if(tvs.isEnabled())
            sDate= tvs.getText().toString();
        if(tve.isEnabled())
            eDate= tve.getText().toString();

        ViewGroup vg = (ViewGroup)v.getParent();
        for(int i=0;i<vg.getChildCount();i++) {
            View vchi = vg.getChildAt(i);

            if (vchi instanceof CheckBox) {
                if(((CheckBox) vchi).isChecked() && ((CheckBox) vchi).getId()!=R.id.checkboxUseStartTimeLimit && ((CheckBox) vchi).getId()!=R.id.checkboxUseEndTimeLimit)
                    keys.add(((CheckBox) vchi).getText().toString());

            }
        }

        Manager m = LocalMemory.getInstance().getManager();
        m.sendMessage(context,title,location,text,centralized,black_list,keys,sDate,eDate);

    }

}
