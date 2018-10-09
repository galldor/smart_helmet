package br.com.fiap.smarthelmet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {


    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.login_input_email);
        passwordText = findViewById(R.id.login_input_password);
        loginButton = findViewById(R.id.login_sing_in_button);
        loginButton.setOnClickListener(onLoginClickListener());
    }


    private View.OnClickListener onLoginClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean canLogin = !emailText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty();
                if(canLogin){
                    Intent homepage = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(homepage);
                }
            }
        };
    }
}
