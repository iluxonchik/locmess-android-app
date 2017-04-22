package pt.ulisboa.tecnico.cmov.locmess;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.locmess.locations.MainLocationsActivity;

public class SignUpActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context=this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(context, LogInActivity.class);
            finish();
            context.startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void logIn(View v) {
        EditText userE = (EditText) findViewById(R.id.editTextRegisterUser);
        EditText passE = (EditText) findViewById(R.id.editTextRegisterPass);
        EditText confirmPassE = (EditText) findViewById(R.id.editTextConfirmRegisterPass);

        Manager m = LocalMemory.getInstance().getManager();
        m.register(context,userE.getText().toString(),passE.getText().toString(),confirmPassE.getText().toString());

    }
}
