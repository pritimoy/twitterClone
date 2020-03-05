package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUserName, edtEmail, edtPassword;
    private Button btnSignup, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtUserName = findViewById(R.id.edtsignupusername);
        edtPassword = findViewById(R.id.edtsignuppassword);
        edtEmail = findViewById(R.id.edtsignupemail);
        btnSignup = findViewById(R.id.btnsignup);
        btnLogin = findViewById(R.id.btnlogin);

        //method for keyboard enter or return key for signup
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCord, KeyEvent event) {
                if (keyCord == KeyEvent.KEYCODE_ENTER &&
                        event.getAction() == KeyEvent.ACTION_DOWN) {

                    onClick(btnSignup);
                }
                return false;
            }
        });

        btnSignup = findViewById(R.id.btnsignup);
        btnLogin = findViewById(R.id.btnlogin);


        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnsignup:

                //check condition for empty input of user
                if (edtUserName.getText().toString().equals("") ||
                        edtEmail.getText().toString().equals("") ||
                        edtPassword.getText().toString().equals("")){
                    FancyToast.makeText(Registration.this,
                            "UserName, Email and Password is  Required " ,
                            Toast.LENGTH_SHORT, FancyToast.INFO, false).show();

                }else {

                    final ParseUser appUser = new ParseUser();
                    appUser.setUsername(edtUserName.getText().toString());
                    appUser.setEmail(edtEmail.getText().toString());
                    appUser.setPassword(edtPassword.getText().toString());


                    appUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(Registration.this, appUser.getUsername()
                                                + "is successfully sign up", Toast.LENGTH_SHORT,
                                        FancyToast.SUCCESS, false).show();
                                startActivity(new Intent(Registration.this,Login.class));

                                finish();

                                //progressDialog.dismiss();
                            } else {
                                FancyToast.makeText(Registration.this, "There was a error "
                                                + e.getMessage(), Toast.LENGTH_SHORT,
                                        FancyToast.ERROR, false).show();
                            }
                        }
                    });
                }
                break;
            case R.id.btnlogin:
                startActivity(new Intent(Registration.this, Login.class));
                break;
        }


    }
}
