package pt.ulisboa.tecnico.cmov.locmess.locations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

        setUpListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListView();
    }

    private void setUpListView() {
        listView = (ListView) this.findViewById(R.id.inbox_list_view);
        LocalMemory locMem = LocalMemory.getInstance();

        if (!locMem.isNotYetAcceptedMessagesAvailable()) {
            View v = this.findViewById(R.id.inbox_no_messages_available);
            if (v != null) {
                v.setVisibility(View.VISIBLE);
            }
        }

        HashSet<Message> msgs = locMem.getNotYetAcceptedMessages();
        MessageAdapter adapter = new MessageAdapter(this, msgs);
        listView.setAdapter(adapter);
    }
}
