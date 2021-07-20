package com.amit.sniffer;

import okhttp3.Response;

public interface HTTPHandlerCallBacks {
    void onSuccess(Response response);
    void onFailure(Response response);
}
