package com.amit.sniffer;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class FileReporter implements Reportable {
    private String mFilePath;
    private Context mContext;

    public FileReporter(Context context, String filePath) {
        this.mContext = context;
        if (!filePath.equalsIgnoreCase("")) {
            this.mFilePath = filePath + ".txt";
        } else {
            this.mFilePath = "HTTPHandlerLogs.txt";
        }
    }

    public FileReporter(Context context) {
        this.mContext = context;
        this.mFilePath = "HTTPHandlerLogs.txt";

    }

    @Override
    public void handleHTTPRequest(String httpStringRepresentation) {
        String[] pathAndFileName = getPathAndFileName();
        File file = new File(mContext.getFilesDir(), pathAndFileName[0]);
        String filename = pathAndFileName[1];
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(httpStringRepresentation.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getPathAndFileName() {
        String[] pathArr = mFilePath.split("/");
        String[] pathAndName = new String[2];
        pathAndName[0] = "";
        if (pathArr.length == 1) {
            pathAndName[1] = pathArr[0];
            return pathAndName;
        }
        for (int i = 0; i < pathArr.length - 1; i++) {
            pathAndName[0] += pathArr[i] + "/";
        }
        pathAndName[1] = pathArr[pathArr.length - 1];
        return pathAndName;
    }
}
