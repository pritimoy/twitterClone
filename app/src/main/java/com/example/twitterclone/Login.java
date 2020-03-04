package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtuseremail);
        edtPassword = findViewById(R.id.edtuserpassword);
        btnLogin = findViewById(R.id.btnlogin);
        btnSignup = findViewById(R.id.btnsignup);

        //implement the enter key or return key on keyboard
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent Event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && Event.getAction() ==KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);

        //check the user already logged in or not
        if(ParseUser.getCurrentUser() != null){

            //goto MainActivity
            transitionToManiActivity();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnlogin:
                //check user input is empty or not
                if (edtEmail.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("")){

                    FancyToast.makeText(Login.this,"Email and Password is Required",
                            FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                } else {
                    ParseUser.logInInBackground(edtEmail.getText().toString(), edtPassword.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null){
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        FancyToast.makeText(Login.this,"Login successful"+user.getUsername(),
                                                FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                                    }
                                    else {
                                        FancyToast.makeText(Login.this,"Login Failed",
                                                FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                                    }
                                }
                            });
                }

                break;
            case R.id.btnsignup:
                startActivity( new Intent(Login.this,Registration.class));
                break;
        }

    }


    private  void transitionToManiActivity(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
