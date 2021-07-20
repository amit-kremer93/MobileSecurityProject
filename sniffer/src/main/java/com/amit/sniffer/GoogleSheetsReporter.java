package com.amit.sniffer;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoogleSheetsReporter implements Reportable {
    public static final String BASE_URL = "https://script.google.com/macros/s/";
    private String mGoogleSheetsID;
    private OkHttpClient mClient;
    private String mTag;

    public GoogleSheetsReporter(String googleSheetsId) {
        this.mGoogleSheetsID = googleSheetsId;
        this.mTag = "HTTPHandler Sheet";
        this.mClient = new OkHttpClient.Builder().readTimeout(3000, TimeUnit.MILLISECONDS).writeTimeout(3000, TimeUnit.MILLISECONDS).build();
    }


    @Override
    public void handleHTTPRequest(String httpStringRepresentation) {
        JSONObject payload = new JSONObject();
        try {
            payload.put("payload", httpStringRepresentation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), payload.toString());
                Request request = new Request.Builder().url(BASE_URL + mGoogleSheetsID + "/exec").post(body).build();
                try (Response response = mClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.i(mTag, "Request sent successfully");
                    } else {
                        Log.i(mTag, "Request wasn't sent");
                    }
                } catch (IOException e) {
                    Log.i(mTag, e.toString());
                }
            }
        }).start();

    }
}
