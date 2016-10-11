package com.spacebunny.hshen.inscat.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.spacebunny.hshen.inscat.R;
import com.spacebunny.hshen.inscat.ins.Ins;
import com.spacebunny.hshen.inscat.ins.auth.Auth;
import com.spacebunny.hshen.inscat.ins.auth.AuthActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    public TextView loginBtn;
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = (TextView) findViewById(R.id.activity_login_btn);

        //Load access token from shared preference
        Ins.init(this);

        if (!Ins.isLoggedIn()) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Auth.openAuthActivity(LoginActivity.this);
                }
            });
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG", "Request code is " + requestCode);
        Log.d("TAG", "Result code is " + resultCode);
        Log.d("TAG", "Data is " + data.getStringExtra(AuthActivity.KEY_CODE));

        if (requestCode == Auth.REQ_CODE && resultCode == RESULT_OK) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            Log.d("TAG", "Auth code is " + authCode);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Time consuming network call is running in non-UI thread
                        String token = Auth.fetchAccessToken(authCode);
                        Ins.login(LoginActivity.this, token);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (IOException | JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
