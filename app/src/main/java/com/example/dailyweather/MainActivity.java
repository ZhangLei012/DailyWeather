package com.example.dailyweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView responseText=findViewById(R.id.weather);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button sendRequest= findViewById(R.id.update_weather);
        responseText=findViewById(R.id.weather);
        Log.d("TAG","11111");
        sendRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(v.getId()==R.id.update_weather){
                    sendRequestWithHttpURLConnection();
                }
            }
        });
    }
    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try{
                    URL url=new URL("http://www.weather.com.cn/data/sk/101010100.html");
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader!=null){
                        try{
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray=null;
                try{
                    jsonArray=new JSONArray(response);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("MainActivity:",jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=null;
                    try{
                        jsonObject=jsonArray.getJSONObject(i);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                responseText.setText(response);
            }
        });
    }
}

