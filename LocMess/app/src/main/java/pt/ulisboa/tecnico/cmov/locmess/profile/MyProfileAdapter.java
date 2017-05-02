package pt.ulisboa.tecnico.cmov.locmess.profile;

/**
 * Created by Valentyn on 20-03-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.R;

public class MyProfileAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private Context context;



    public MyProfileAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
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
        return 0;
        //just return 0 if your list items do not have an Id variable.
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

        elementBtn.setText(list.get(position));

        elementBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(context, ViewKeyActivity.class);
                String[] element = elementBtn.getText().toString().split(":");
                String key = element[0];
                String value = element[1];

                myIntent.putExtra("KEY", key);
                myIntent.putExtra("VALUE", value);

                ((Activity)context).startActivityForResult(myIntent,22);

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Manager m = LocalMemory.getInstance().getManager();
                String[] splited = list.get(position).split(":");
                LocalMemory.getInstance().setStartAct(true);
                m.removeKey(context,splited[0],splited[1]);

            }
        });

        return view;
    }
}
