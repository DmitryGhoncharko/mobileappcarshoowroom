package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.connection.CarsDBHelper;
import com.example.myapplication.connection.FavoritesDBHelper;
import com.example.myapplication.connection.UserDBHelper;
import com.example.myapplication.entity.Car;
import com.example.myapplication.utils.CarsParser;
import com.example.myapplication.utils.InMemoryUserIdCache;
import com.example.myapplication.utils.NetworkTester;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CarsActivity extends AppCompatActivity{
    private  List<Car> cars = new ArrayList<>();
    private FavoritesDBHelper favoritesDBHelper;
    private boolean hasInternet;
    private HttpURLConnection conn;
    private CarsDBHelper carsDBHelper;
    private Thread thread;
    private LinearLayout layout;
    private Button button;
    private static boolean sort;
    private Button sortB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars2);
        layout = findViewById(R.id.linearLayout);
        favoritesDBHelper = new FavoritesDBHelper(this);
        hasInternet = NetworkTester.isNetworkAvailable(this);
        carsDBHelper = new CarsDBHelper(this);
        sortB = findViewById(R.id.sort);
        sortB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sort){
                    sort = false;
                }else sort = true;
                startActivity(new Intent(CarsActivity.this,CarsActivity.class));
            }
        });
        button = findViewById(R.id.faviritesBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CarsActivity.this,FavoritesActivity.class));
            }
        });
            thread =  new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                InputStream inputStream = null;
                SQLiteDatabase sqLiteDatabase = null;
                if(hasInternet){
                    try{
                        URL url = new URL("http://194.87.98.149:80/cars/");

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        String res = "";
                        String line;
                        while ((line = bufferedReader.readLine())!=null){
                            res+=line;
                        }

                        cars = CarsParser.parse(res);
                        sqLiteDatabase = carsDBHelper.getWritableDatabase();
                        URL imageUrl = null;

                        for(Car car: cars){
                            imageUrl = new URL("http://194.87.98.149:8000/" + car.getCarId() + ".png");
                            inputStream  = imageUrl.openStream();
                            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            Files.copy(inputStream, Paths.get(path +"/" + car.getCarId() + ".png"), StandardCopyOption.REPLACE_EXISTING);

                            ContentValues contentValues = new ContentValues();
                            contentValues.put(CarsDBHelper.COLUMN_ID_NAME,car.getCarId());
                            contentValues.put(CarsDBHelper.COLUMN_CAR_NAME,car.getCarName());
                            contentValues.put(CarsDBHelper.COLUMN_DESCRIPTION_NAME,car.getCarDescription());
                            sqLiteDatabase.insert(CarsDBHelper.TABLE_NAME,null,contentValues);
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }finally {
                        if(sqLiteDatabase!=null){
                            sqLiteDatabase.close();
                        }
                        if(conn!=null){
                            conn.disconnect();
                        }
                        if(inputStream!=null){
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }else {
                    Cursor cursor = carsDBHelper.getReadableDatabase().query(CarsDBHelper.TABLE_NAME, null, null,null,null,null,null);
                    if(cursor.moveToFirst()){
                        int carIdIndex = cursor.getColumnIndex(CarsDBHelper.COLUMN_ID_NAME);
                        int carNameIndex = cursor.getColumnIndex(CarsDBHelper.COLUMN_CAR_NAME);
                        int carDescriptionIndex = cursor.getColumnIndex(CarsDBHelper.COLUMN_DESCRIPTION_NAME);
                        do{
                            int carId = cursor.getInt(carIdIndex);
                            String carName = cursor.getString(carNameIndex);
                            String carDescription = cursor.getString(carDescriptionIndex);
                            Car car = new Car.Builder().
                                    withCarId(carId).
                                    withCarName(carName).
                                    withCarDescription(carDescription).
                                    build();
                            cars.add(car);
                        }while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }
        });
            thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(sort) {
            Collections.sort(cars, new Comparator<Car>() {
                @Override
                public int compare(Car car, Car t1) {
                    return car.getCarName().length()-t1.getCarName().length();
                }
            });
        }
        for(Car car : cars){
            TextView carName = new TextView(CarsActivity.this);
            carName.setText("???????????????? ????????????:\n" +car.getCarName());
            ImageView imageView = new ImageView(CarsActivity.this);
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            imageView.setImageDrawable(Drawable.createFromPath(path +"/" + car.getCarId() + ".png"));
            TextView carDescription = new TextView(CarsActivity.this);
            carDescription.setText("???????????????? ????????????:\n" + car.getCarDescription());
            Button button = new Button(CarsActivity.this);
            button.setText("???????????????? ?? ??????????????????");
            button.setId((int) car.getCarId());
            button.setOnClickListener(listener);
            layout.addView(carName, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(carDescription, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(button, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        URL url = new URL("http://194.87.98.149:80/favorites/add/");
                        Map<String, Object> params = new LinkedHashMap<>();
                        params.put("userId", InMemoryUserIdCache.userId);
                        params.put("carId", view.getId());

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
                        conn.setReadTimeout(800);
                        conn.setConnectTimeout(800);
                        conn.getOutputStream().write(postDataBytes);
                        if (HttpURLConnection.HTTP_CREATED == conn.getResponseCode()) {
                            System.out.println("NICE");
                            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                            StringBuilder userId = new StringBuilder();
                            for (int c; (c = in.read()) >= 0; ) {
                                userId.append((char) c);
                            }

                            SQLiteDatabase sqLiteDatabase = favoritesDBHelper.getWritableDatabase();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(FavoritesDBHelper.COLUMN_ID_NAME, Integer.valueOf(userId.toString()));
                            contentValues.put(FavoritesDBHelper.COLUMN_USER_ID_NAME, InMemoryUserIdCache.userId);
                            contentValues.put(FavoritesDBHelper.COLUMN_CAR_ID_NAME, view.getId());
                            sqLiteDatabase.insert(favoritesDBHelper.TABLE_NAME, null, contentValues);
                            return;
                        }
                    }catch (Exception e){
                        System.out.println(e);
                    }finally {

                    }
                    }
            }).start();
        }
    };
}