# MobileSecurityProject

HTTP client, built on top of okhttp3 that can sniff the request information and send it to different "Reporters"<br>
The reporters are: local text file, Console (Logs) and Google sheets

# Download
## Requirement
```
minSdkVersion 23
```
## Repository
Add this in your root `build.gradle` file (**not** your module `build.gradle` file):
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
## Dependency
Add this to your module's `build.gradle` file
```
dependencies {
    implementation 'com.github.amit-kremer93:MobileSecurityProject:1.0.0'
}
```

# How the request looks like?
The handler collects the following:<br>
Method, target url, headers, status code and how much time did it took<br>
<img src ="pics/2.png" width="750"> 


# Reporters
Reporter is an end-point, that handles the request in different way. in this repo there are 3 reporters so far:
### FileReporter
saves request information to file. gets `context` and `file name` in the constructor.
```java
HTTPHandler.getInstance().addReporter(new FileReporter(this, fileNme));
```
### ConsoleReporter
Prints the request to Logs. gets `tag (Log.i(tag, ""))` in the constructor.
```java
HTTPHandler.getInstance().addReporter(new ConsoleReporter());
```
### GoogleSheetsReporter
Saves requests in google sheets document. get `documentId` in the constructor.<br>
```java
HTTPHandler.getInstance().addReporter(new GoogleSheetsReporter("AKfycbz0aizzzXhB3Q1xXxX3sOPku_CC3obD0SH8YS1DZI5XVBFjjRFdecWfDZwgU_-RqY"));
```
inside your sheet's script editor you need to add the following script:
```javascript
function doPost (e) {
  const body = e.postData.contents;
  const bodyJSON = JSON.parse(body);
  const ss = SpreadsheetApp.getActiveSpreadsheet();
  const ws = ss.getSheetByName("test");
  ws.appendRow([bodyJSON.payload]);
}
``` 
# How to use
init the handler in the activity with context:
```java
HTTPHandler.init(this);
```
add desired reporters:
```java
HTTPHandler.getInstance().addReporter(new ConsoleReporter());
HTTPHandler.getInstance().addReporter(new FileReporter(this));
HTTPHandler.getInstance().addReporter(new GoogleSheetsReporter("AKfycbz0a5XVBFjjRFdecWfDZwgU_-RqY"));
```
send the request:
```java
  HTTPHandler.getInstance()
             .setRequestBody(JSONObject) //if any. GET doesn't need
             .setMethod(HTTPHandler.METHOD.POST) // OR HTTPHandler.METHOD.GET
             .setCompletionHandler(new HTTPHandlerCallBacks() {
                 @Override
                    public void onSuccess(Response response) {
                        //do someting after success
                    }

                 @Override
                    public void onFailure(Response response) {
                        //do someting after failure
                    }
                 })
             .setUrl("https://httpbin.org/post")
             .start();
```

