package pt.ulisboa.tecnico.cmov.locmess.locations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.HashSet;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.locations.adapters.MessageAdapter;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;

public class InboxMessagesActivity extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_locations);

        listView = (ListView) this.findViewById(R.id.inbox_list_view);

        HashSet<Message> msgs = LocalMemory.getInstance().getNotYetAcceptedMessages();
        MessageAdapter adapter = new MessageAdapter(this, msgs);
        listView.setAdapter(adapter);
    }
}
