package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static pt.ulisboa.tecnico.cmov.locmess.R.id.datePicker;

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

        DatePicker dp = (DatePicker) findViewById(datePicker);
        dp.setEnabled(false);
    }

    public void useTimeLimit(View v) {
        CheckBox ck = (CheckBox) findViewById(R.id.checkboxUseTimeLimit);
        DatePicker dp = (DatePicker) findViewById(datePicker);
        if(ck.isChecked())
            dp.setEnabled(true);
        else
            dp.setEnabled(false);

    }

    public void sendMessage(View v) {
        boolean centralized=true;
        boolean black_list=true;
        Spinner delivery = (Spinner) findViewById(R.id.spinnerDeliveryMode);
        Spinner policy = (Spinner) findViewById(R.id.spinnerPolicyType);
        DatePicker dp = (DatePicker) findViewById(datePicker);

        if(!delivery.getSelectedItem().equals("Centralized"))
            centralized=false;
        if(!policy.getSelectedItem().equals(("Black list")))
            black_list=false;

        List<String> keys = new ArrayList<>();

        int day = dp.getDayOfMonth();
        int month = dp.getMonth();
        int year =  dp.getYear();

        MyDate date = null;

        CheckBox ck = (CheckBox) findViewById(R.id.checkboxUseTimeLimit);
        if(ck.isChecked())
            date= new MyDate(day,month,year);

        Message msg = new Message(0,title,LocalMemory.getInstance().getLoggedUserMail(),location,text,centralized,black_list,keys,date);

        LocalMemory.getInstance().addMessage(msg);
        Intent intent = new Intent(this, MainMessagesActivity.class);
        startActivity(intent);
    }

}
