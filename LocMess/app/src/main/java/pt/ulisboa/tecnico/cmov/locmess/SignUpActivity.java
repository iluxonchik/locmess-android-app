package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context=this;
    }

    public void logIn(View v) {
        EditText userE = (EditText) findViewById(R.id.editTextRegisterUser);
        EditText passE = (EditText) findViewById(R.id.editTextRegisterPass);
        EditText confirmPassE = (EditText) findViewById(R.id.editTextConfirmRegisterPass);

        Manager m = LocalMemory.getInstance().getManager();
        m.register(context,userE.getText().toString(),passE.getText().toString(),confirmPassE.getText().toString());

    }
}
