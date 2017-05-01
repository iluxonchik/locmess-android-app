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
    import pt.ulisboa.tecnico.cmov.locmess.messages.Message;

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
        TextView tDate = (TextView) findViewById(R.id.textViewMDate);

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
                tDate.setText("xx");
            }
            else
                tDate.setText("-/-/-");

        }

    }
}
