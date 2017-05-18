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
import java.util.HashSet;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;

public class MainMessagesActivity extends AppCompatActivity {

    private List<String> messages;
    private List<String> my_messages;
    private HashSet<String> authoredMessages;

    public Context context;
    private MyMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_messages);

        context=this;

        messages = new ArrayList<>();
        my_messages = new ArrayList<>();
        authoredMessages = new HashSet<>();

        LocalMemory locMem = LocalMemory.getInstance();

        List<Message> msgs = locMem.getMessages();
        List<Message> msgsD = locMem.getDecentralizedMessages();
        List<Message> acceptedMessages = locMem.getAcceptedMessages();
        msgs.addAll(acceptedMessages);

        List<Message> msgsDTS = LocalMemory.getInstance().getDecentralizedmessagesToSend();

        String msgTitle;

        for(int i=0;i<msgs.size();i++){
            msgTitle = msgs.get(i).getId()+": "+msgs.get(i).getTitle();
            messages.add(msgTitle);
            if(msgs.get(i).getAuthor().equals(LocalMemory.getInstance().getLoggedUserMail())) {
                my_messages.add(msgTitle);
                authoredMessages.add(msgTitle);
            }
        }

        for(int i=0;i<msgsD.size();i++){
            msgTitle = msgs.get(i).getId()+": "+msgs.get(i).getTitle();
            messages.add(msgTitle);
            if(msgsD.get(i).getAuthor().equals(LocalMemory.getInstance().getLoggedUserMail())) {
                my_messages.add(msgTitle);
                authoredMessages.add(msgTitle);
            }
        }

        for(int i=0;i<msgsDTS.size();i++){
            msgTitle = msgs.get(i).getId()+": "+msgs.get(i).getTitle();
            messages.add(msgTitle);
            if(msgsDTS.get(i).getAuthor().equals(LocalMemory.getInstance().getLoggedUserMail())) {
                my_messages.add(msgTitle);
                authoredMessages.add(msgTitle);
            }
        }

        populateListView(messages);
    }

    private void populateListView(List<String> l) {
        adapter = new MyMessagesAdapter(l, this , authoredMessages);
        ListView list = (ListView) findViewById(R.id.listViewMessages);
        list.setAdapter(adapter);
    }

    public void showMyMessagesOnly(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.checkboxSeeMyMessages);
        if (cb.isChecked()) {
            populateListView(my_messages);
        }
        else {
            populateListView(messages);
        }

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
