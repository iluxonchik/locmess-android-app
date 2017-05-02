    package pt.ulisboa.tecnico.cmov.locmess.messages;

    import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.R;

    public class ViewMessageActivity extends AppCompatActivity {

     private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        context=this;

        Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        Message m = LocalMemory.getInstance().getMessage(Integer.parseInt((id)));

        TextView tId = (TextView) findViewById(R.id.textViewMId);
        TextView tTitle = (TextView) findViewById(R.id.textViewMTitle);
        TextView tAutor = (TextView) findViewById(R.id.textViewMAutor);
        TextView tLocation = (TextView) findViewById(R.id.textViewMLocation);
        TextView tText = (TextView) findViewById(R.id.textViewMText);
        TextView tDelivery = (TextView) findViewById(R.id.textViewMDelivery);
        TextView tPolicy = (TextView) findViewById(R.id.textViewMPolicy);
        ListView lKeys = (ListView) findViewById(R.id.listViewListPreferencesView);
        TextView tSDate = (TextView) findViewById(R.id.textViewMSDate);
        TextView tEDate = (TextView) findViewById(R.id.textViewMEDate);

        if(m!=null){
            tId.setText(""+m.getId());
            tTitle.setText(m.getTitle());
            tAutor.setText(m.getAutor());
            tLocation.setText(m.getLocation());
            tText.setText(m.getText());
            if(m.isCentralized())
                tDelivery.setText("Centralized");
            else
                tDelivery.setText("Descentralized");

            if(m.isBlackList())
                tPolicy.setText("Black list");
            else
                tPolicy.setText("White list");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,m.getKeys());
            lKeys.setAdapter(arrayAdapter);

            if(m.getStartDate()!=null){
                if(m.getStartDate().contains("."))
                    tSDate.setText(m.getStartDate().substring(0,m.getStartDate().indexOf('.')));
                else
                    tSDate.setText(m.getStartDate());            }
            else
                tSDate.setText("YYYY-MM-DDThh:mm");

            if(m.getEndDate()!=null){
                if(m.getEndDate().contains(".")) {
                    if(m.getEndDate().substring(0, m.getEndDate().indexOf('.')).equals("9999-12-31T23:59:59"))
                        tEDate.setText("");
                    else
                        tEDate.setText(m.getEndDate().substring(0, m.getEndDate().indexOf('.')));
                }
                else
                    tEDate.setText(m.getEndDate());
            }
            else
                tEDate.setText("YYYY-MM-DDThh:mm");

        }

    }
}
