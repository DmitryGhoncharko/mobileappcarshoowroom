package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.exception.ServiceError;


public class RegistrationActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        login = findViewById(R.id.loginRegValue);
        password = findViewById(R.id.passRegVakue);
        registrationButton = findViewById(R.id.registrationButton);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                } catch (ServiceError serviceError) {
                    Toast.makeText(RegistrationActivity.this, R.string.invalid_login_message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}