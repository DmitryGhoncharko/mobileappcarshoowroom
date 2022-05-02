package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.connection.HikariCPConnectionPool;
import com.example.myapplication.securiy.BcryptWithSaltHasherImpl;
import com.example.myapplication.validator.SimpleUserValidator;

import java.util.Optional;

import model.dao.SimpleUserDao;
import model.entity.User;
import model.service.SimpleUserService;
import model.service.UserService;

public class LoginActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button loginButton;
    private Button restorePassButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginValue);
        password = findViewById(R.id.passwordValue);
        loginButton = findViewById(R.id.laginInApp);
        restorePassButton = findViewById(R.id.restorePass);
        UserService userService = new SimpleUserService(new SimpleUserValidator(),new SimpleUserDao(new HikariCPConnectionPool()),new BcryptWithSaltHasherImpl());
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Optional<User> userFromDB =  userService.authenticateIfClient(login.getText().toString(), password.getText().toString());
                if(userFromDB.isPresent()){
                    startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this,R.string.invalid_login_message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}