package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        context=this;

    }

    public void register(View v) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void logIn(View v) {
        EditText userE = (EditText) findViewById(R.id.etUser);
        EditText passE = (EditText) findViewById(R.id.etPassword);

        Manager m = LocalMemory.getInstance().getManager();
        m.login(context,userE.getText().toString(),passE.getText().toString());

    }
}
