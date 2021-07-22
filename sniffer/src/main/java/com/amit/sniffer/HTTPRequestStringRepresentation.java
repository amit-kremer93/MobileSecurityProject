package com.amit.sniffer;

public class HTTPRequestStringRepresentation {
    private long mStartTime;
    private long mEndTime;
    private HTTPHandler.METHOD mMethod;
    private int mStatusCode;
    private String mHeaders;
    private String mUrl;
    private boolean isWifiConnected;

    public HTTPRequestStringRepresentation() {
    }

    public boolean isWifiConnected() {
        return isWifiConnected;
    }

    public void setWifiConnected(boolean wifiConnected) {
        isWifiConnected = wifiConnected;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmMethod(HTTPHandler.METHOD mMethod) {
        this.mMethod = mMethod;
    }

    public void setmStatusCode(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }

    public void setHeaders(String message) {

        this.mHeaders = "{" + message.replaceAll("\n", ", ") + "}";
    }

    public long getmStartTime() {
        return mStartTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public HTTPHandler.METHOD getmMethod() {
        return mMethod;
    }

    public int getmStatusCode() {
        return mStatusCode;
    }

    public String getHeaders() {
        return mHeaders;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public String toString() {
        return "~~> " + mMethod + ": " + mUrl + ", Headers: " + mHeaders + " sent on Wifi: "+ isWifiConnected+ " and returned with status code " + mStatusCode + " in " + (mEndTime - mStartTime) + " miliseconds\n";
    }
}
