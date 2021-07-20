package com.amit.sniffer;

import android.util.Log;

public class ConsoleReporter implements Reportable {
    private String mTag;
    public ConsoleReporter(){
        this.mTag = "HTTPHandler Console";
    }
    public ConsoleReporter(String tag){
        this.mTag = tag;
    }
    @Override
    public void handleHTTPRequest(String httpStringRepresentation) {
        Log.i(mTag, httpStringRepresentation);
    }
}
