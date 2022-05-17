package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.connection.UserDBHelper;
import com.example.myapplication.exception.ServiceError;
import com.example.myapplication.utils.InMemoryUserIdCache;
import com.example.myapplication.utils.NetworkTester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button registrationButton;
    private boolean hasInternetConnection;
    private UserDBHelper userDBHelper;
    private HttpURLConnection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        login = findViewById(R.id.loginRegValue);
        password = findViewById(R.id.passRegVakue);
        registrationButton = findViewById(R.id.registrationButton);
        userDBHelper = new UserDBHelper(this);
        hasInternetConnection = NetworkTester.isNetworkAvailable(this);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       if(hasInternetConnection){
                           try{
                               URL url = new URL("http://194.87.98.149:80/user/registration/");
                               Map<String, Object> params = new LinkedHashMap<>();
                               params.put("login", login.getText().toString());
                               params.put("password", password.getText().toString());

                               StringBuilder postData = new StringBuilder();
                               for (Map.Entry<String, Object> param : params.entrySet()) {
                                   if (postData.length() != 0) postData.append('&');
                                   postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                                   postData.append('=');
                                   postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                               }
                               byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                               conn = (HttpURLConnection) url.openConnection();
                               conn.setRequestMethod("POST");
                               conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                               conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                               conn.setDoOutput(true);
                               conn.setDoInput(true);
                               conn.getOutputStream().write(postDataBytes);
                               if (HttpURLConnection.HTTP_CREATED == conn.getResponseCode()) {
                                   System.out.println("NICE");
                                   Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                   StringBuilder userId = new StringBuilder();
                                   for (int c; (c = in.read()) >= 0; ) {
                                       userId.append((char) c);
                                   }
                                   SQLiteDatabase sqLiteDatabase = userDBHelper.getWritableDatabase();
                                   ContentValues contentValues = new ContentValues();
                                   contentValues.put(UserDBHelper.COLUMN_ID_NAME, Integer.valueOf(userId.toString()));
                                   contentValues.put(UserDBHelper.COLUMN_LOGIN_NAME, login.getText().toString());
                                   contentValues.put(UserDBHelper.COLUMN_PASSWORD_NAME, password.getText().toString());
                                   sqLiteDatabase.insert(UserDBHelper.TABLE_NAME, null, contentValues);
                                   startActivity(new Intent(RegistrationActivity.this, CarsActivity.class));
                                   return;
                               } else {
                                   Toast toast = Toast.makeText(getApplicationContext(),
                                           "Неверный логин или пароль", Toast.LENGTH_SHORT);
                                   toast.show();
                                   return;
                               }
                           }catch (Exception e){

                           }
                       }else {
                           Toast toast = Toast.makeText(getApplicationContext(),
                                   "Неверный логин или пароль", Toast.LENGTH_SHORT);
                           toast.show();
                       }
                   }
               }).start();
            }
        });
    }
}