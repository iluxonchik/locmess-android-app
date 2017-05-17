package pt.ulisboa.tecnico.cmov.locmess.locations.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.messages.Message;
import pt.ulisboa.tecnico.cmov.locmess.messages.ViewMessageActivity;

/**
 * Created by iluxo on 17/05/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private static final String LOG_TAG = MessageAdapter.class.getName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Message> messages;

    public MessageAdapter(Context context, HashSet<Message> items) {
        this.context = context;
        this.messages = new ArrayList<>(items);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.list_message, parent, false);
        TextView title = (TextView) row.findViewById(R.id.inbox_title);
        TextView text = (TextView) row.findViewById(R.id.inbox_text);
        TextView location = (TextView) row.findViewById(R.id.inbox_location);

        Message msg = (Message) getItem(position);

        title.setText(msg.getTitle());
        text.setText(msg.getText());
        location.setText(msg.getLocation());

        row.setTag(msg);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = (Message) v.getTag();
                LocalMemory.getInstance().acceptMessage(msg);
                Log.d(MessageAdapter.LOG_TAG, "Message with id " + msg.getId() + " accepted!");

                Intent i = new Intent(context, ViewMessageActivity.class);
                i.putExtra("ID", new Integer(msg.getId()).toString());
                context.startActivity(i);
            }
        });

        return row;
    }
}
