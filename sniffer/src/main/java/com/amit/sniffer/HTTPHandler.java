package com.amit.sniffer;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HTTPHandler {
    public static final String TAG = "HTTPHandler";
    private static HTTPHandler instance;
    private OkHttpClient mClient;
    private HTTPHandlerCallBacks mHttpHandlerCallBacks;
    private JSONObject mBody;
    private ArrayList<Reportable> mReporters;
    private String mUrl;
    private METHOD mMethod;


    private HTTPHandler() {
        mClient = new OkHttpClient.Builder().readTimeout(3000, TimeUnit.MILLISECONDS).writeTimeout(3000, TimeUnit.MILLISECONDS).build();
        mReporters = new ArrayList<Reportable>();
    }

    public static synchronized HTTPHandler getInstance() {
        if (instance == null) {
            instance = new HTTPHandler();
        }
        return instance;
    }

    public HTTPHandler setCompletionHandler(HTTPHandlerCallBacks completionHandler) {
        mHttpHandlerCallBacks = completionHandler;
        return instance;
    }

    public HTTPHandler setRequestBody(JSONObject json) {
        mBody = json;
        return instance;
    }

    public HTTPHandler setUrl(String url) {
        mUrl = url;
        return instance;
    }

    public HTTPHandler setMethod(METHOD method) {
        mMethod = method;
        return instance;
    }

    public void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HTTPRequestStringRepresentation httpStr = new HTTPRequestStringRepresentation();
                httpStr.setmMethod(mMethod);
                httpStr.setmUrl(mUrl);
                httpStr.setmStartTime(System.currentTimeMillis());
                Request request = null;
                switch (mMethod) {
                    case GET:
                        request = new Request.Builder().url(mUrl).get().build();
                        break;
                    case POST:
                        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), mBody.toString());
                        request = new Request.Builder().url(mUrl).post(body).build();
                        break;
                    default:
                        break;
                }
                try (Response response = mClient.newCall(request).execute()) {
                    httpStr.setmStatusCode(response.code());
                    httpStr.setHeaders(response.headers().toString());
                    httpStr.setmEndTime(System.currentTimeMillis());
                    if (response.isSuccessful()) {
                        if (mHttpHandlerCallBacks != null) {
                            mHttpHandlerCallBacks.onSuccess(response);
                        } else {
                            Log.d(TAG, "Request sent successfully");
                        }
                    } else {
                        if (mHttpHandlerCallBacks != null) {
                            mHttpHandlerCallBacks.onFailure(response);
                        } else {
                            Log.d(TAG, "Request wasn't sent");
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                } finally {
                    sendToReporters(httpStr.toString());
                    clearInstance();
                }
            }
        });
        thread.start();
    }

    private void clearInstance() {
        this.mBody = null;
        this.mHttpHandlerCallBacks = null;
    }

    public void addReporter(Reportable reportable) {
        this.mReporters.add(reportable);
    }

    public void removeReporter(Reportable reportable) {
        this.mReporters.remove(reportable);
    }

    private void sendToReporters(String httpStringRepresentation) {
        for (Reportable reportable : this.mReporters) {
            reportable.handleHTTPRequest(httpStringRepresentation);
        }
    }

    public enum METHOD {
        POST, GET
    }
}
