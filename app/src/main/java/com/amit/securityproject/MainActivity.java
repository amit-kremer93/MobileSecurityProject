package com.amit.securityproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amit.sniffer.ConsoleReporter;
import com.amit.sniffer.FileReporter;
import com.amit.sniffer.GoogleSheetsReporter;
import com.amit.sniffer.HTTPHandler;
import com.amit.sniffer.HTTPHandlerCallBacks;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton postBTN = findViewById(R.id.post);
        MaterialButton getBTN = findViewById(R.id.get);
        HTTPHandler.init(this);
        HTTPHandler.getInstance().addReporter(new ConsoleReporter());
        HTTPHandler.getInstance().addReporter(new FileReporter(this));
        HTTPHandler.getInstance().addReporter(new GoogleSheetsReporter("AKfycbz0aizzzXhB3Q1owyfwG3sOPku_CC3obD0SH8YS1DZI5XVBFjjRFdecWfDZwgU_-RqY"));


        postBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("payload", "test_payload");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                HTTPHandler.getInstance()
                        .setRequestBody(json)
                        .setMethod(HTTPHandler.METHOD.POST)
                        .setCompletionHandler(new HTTPHandlerCallBacks() {
                            @Override
                            public void onSuccess(Response response) {
                            }

                            @Override
                            public void onFailure(Response response) {
                            }
                        })
                        .setUrl("https://httpbin.org/post")
                        .start();
            }
        });

        getBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPHandler.getInstance()
                        .setUrl("https://httpbin.org/get")
                        .setMethod(HTTPHandler.METHOD.GET)
                        .setCompletionHandler(new HTTPHandlerCallBacks() {
                            @Override
                            public void onSuccess(Response response) {
                            }

                            @Override
                            public void onFailure(Response response) {
                            }
                        })
                        .start();
            }
        });
    }
}