package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;

public class MainMessagesActivity extends AppCompatActivity {

    private List<String> messages = new ArrayList<>();
    private List<String> my_messages = new ArrayList<>();
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_messages);

        context=this;

        List<Message> msgs = LocalMemory.getInstance().getMessages();
        List<Message> msgsD = LocalMemory.getInstance().getDecentralizedMessages();
        List<Message> msgsDTS = LocalMemory.getInstance().getDecentralizedmessagesToSend();
        msgs.addAll(msgsD);
        msgs.addAll(msgsDTS);


        for(int i=0;i<msgs.size();i++){
            messages.add(msgs.get(i).getId()+":"+msgs.get(i).getTitle());
            if(msgs.get(i).getAutor().equals(LocalMemory.getInstance().getLoggedUserMail()))
                my_messages.add(msgs.get(i).getId()+":"+msgs.get(i).getTitle());
        }

        populateListView(messages);
    }

    private void populateListView(List<String> l) {
        MyMessagesAdapter adapter = new MyMessagesAdapter(l,this);
        ListView list = (ListView) findViewById(R.id.listViewMessages);
        list.setAdapter(adapter);
    }

    public void showMyMessagesOnly(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkboxSeeMyMessages);
        if (cb.isChecked())
            populateListView(my_messages);
        else
            populateListView(messages);

    }

    public void addMessage(View v) {
        Intent intent = new Intent(this, AddMessageActivity.class);
        ((Activity)context).startActivityForResult(intent,21);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 21) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }

}
