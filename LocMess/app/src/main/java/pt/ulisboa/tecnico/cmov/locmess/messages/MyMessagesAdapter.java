package pt.ulisboa.tecnico.cmov.locmess.messages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.locations.adapters.MessageAdapter;

/**
 * Created by Valentyn on 22-03-2017.
 */

public class MyMessagesAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;
    private HashSet<String> authoredMessages;

    public MyMessagesAdapter(List<String> list, Context context, HashSet<String> authoredMessages) {
        this.list = list;
        this.context = context;
        this.authoredMessages = authoredMessages;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle buttons and add onClickListeners
        final Button elementBtn = (Button)view.findViewById(R.id.buttonListElement);
        final Button deleteBtn = (Button)view.findViewById(R.id.buttonListRemove);

        final String thisMsgTitle = (String) getItem(position);

        if (!authoredMessages.contains(thisMsgTitle)) {
            deleteBtn.setVisibility(View.GONE);
        }

        elementBtn.setText(list.get(position));

        elementBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(context, ViewMessageActivity.class);
                String message = elementBtn.getText().toString();

                String[] splited = message.split(":");
                myIntent.putExtra("ID", splited[0]);

                context.startActivity(myIntent);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String[] splited = list.get(position).toString().split(":");

                Manager m = LocalMemory.getInstance().getManager();
                m.removeMessage(context,Integer.parseInt(splited[0]));
            }
        });

        return view;
    }
}
