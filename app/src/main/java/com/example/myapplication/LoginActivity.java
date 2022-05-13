package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.myapplication.connection.UserDBHelper;
import com.example.myapplication.utils.NetworkTester;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private EditText login;
    private EditText password;
    private Button loginButton;
    private Button restorePassButton;
    private UserDBHelper userDBHelper;
    private HttpURLConnection conn;
    private boolean hasInternetConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginValue);
        password = findViewById(R.id.passwordValue);
        loginButton = findViewById(R.id.laginInApp);
        restorePassButton = findViewById(R.id.restorePass);
        userDBHelper = new UserDBHelper(this);
        hasInternetConnection = NetworkTester.isNetworkAvailable(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (hasInternetConnection) {
                                URL url = new URL("http://194.87.98.149:80/user/login/");
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
                                if (HttpURLConnection.HTTP_ACCEPTED == conn.getResponseCode()) {
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
                                    startActivity(new Intent(LoginActivity.this, CarsActivity.class));
                                    return;
                                } else {
                                    startActivity(new Intent(LoginActivity.this,ErrorPageActivity.class));
                                    return;
                                }
                            } else {
                                Cursor cursor = userDBHelper.getReadableDatabase().query(UserDBHelper.TABLE_NAME, null, null, null, null, null, null);
                                if (cursor.moveToFirst()) {
                                    int userIdIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_ID_NAME);
                                    int userLoginIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_LOGIN_NAME);
                                    int userPasswordIndex = cursor.getColumnIndex(UserDBHelper.COLUMN_PASSWORD_NAME);
                                    do {
                                        int userId = cursor.getInt(userIdIndex);
                                        String userLogin = cursor.getString(userLoginIndex);
                                        String userPassword = cursor.getString(userPasswordIndex);
                                        String userLoginFromTextField = login.getText().toString();
                                        String userPasswordFromTextField = password.getText().toString();
                                        if (userLogin.equals(userLoginFromTextField) && userPassword.equals(userPasswordFromTextField)) {
                                            cursor.close();
                                            startActivity(new Intent(LoginActivity.this, CarsActivity.class));
                                            return;
                                        }
                                    } while (cursor.moveToNext());
                                }
                                cursor.close();
                                startActivity(new Intent(LoginActivity.this,ErrorPageActivity.class));
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }finally {
                            if(conn!=null){
                                conn.disconnect();
                            }
                        }
                    }
                }).start();
            }
        });
    }
}