package pt.ulisboa.tecnico.cmov.locmess.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.locmess.LocalMemory;
import pt.ulisboa.tecnico.cmov.locmess.Manager;
import pt.ulisboa.tecnico.cmov.locmess.R;
import pt.ulisboa.tecnico.cmov.locmess.main.SignUpActivity;

public class LogInActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        context=this;

        final TextView txtView = (TextView) this.findViewById(R.id.tvRegister);
        txtView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                register(v);
            }
        });

    }

    public void register(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

        finish();
    }

    public void logIn(View v) {
        EditText userE = (EditText) findViewById(R.id.etUser);
        EditText passE = (EditText) findViewById(R.id.etPassword);

        Manager m = LocalMemory.getInstance().getManager();
        m.login(context,userE.getText().toString(),passE.getText().toString());
    }
}
