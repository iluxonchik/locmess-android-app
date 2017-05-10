package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
            checkBox.setId(i+2);
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            //FIXME
            if(i>0)
                layoutParam.addRule(RelativeLayout.BELOW, i+2 - 1);
            else
                layoutParam.addRule(RelativeLayout.ALIGN_PARENT_START);

            checkBox.setLayoutParams(layoutParam);
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

        if(!delivery.getSelectedItem().equals("Centralized")) {
            centralized=false;
        }

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

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.layoutProfilePreferences);
        for(int i=0;i< rl.getChildCount();i++){
            if(((CheckBox)rl.getChildAt(i)).isChecked())
                keys.add(((CheckBox)rl.getChildAt(i)).getText().toString());
        }


        Manager m = LocalMemory.getInstance().getManager();
        if(centralized)
            m.sendMessage(context,title,location,text,centralized,black_list,keys,sDate,eDate);
        else
            m.decentralizedMessageToSend(context,title,location,text,centralized,black_list,keys,sDate,eDate);
    }

}
