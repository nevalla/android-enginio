qtc-sdk-android
===============

An asynchronous, callback-based [Enginio](https://engin.io/) client library for Android built on top of [Android Asynchronous Http Client](https://github.com/loopj/android-async-http) and Apache's [HttpClient](http://hc.apache.org/httpcomponents-client-ga/) libraries.
Installation
------------
1. ADT (Eclipse)
Clone this repo and add EnginioClient to your project as library

Features
--------
###High-level API
1. Overwrite EnginioObject class
```
public class Todo extends EnginioObject {

	public Todo() {
		super("todo"); // pass objectType parameter to superclass's constructor
	}
	
}
```

2. Use your class in code
```
EnginioRestClient.ENGINIO_BACKEND_ID = "Your backend ID";
Todo todo = new Todo(); 
todo.put("title", "My first todo");
todo.save(new EnginioHttpResponseHandler() {
  	@Override
  	public void onSuccess(int statusCode,
  			Object response) {
    		// handle UI changes etc
    	}
});
```

###Low-level REST API
Use the following EnginioRestClient's static methods. See [Android Asynchronous Http Client](https://github.com/loopj/android-async-http) for more details.
```
public static void get(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler)
public static void post(String url, String payload,
			JsonHttpResponseHandler responseHandler)
public static void delete(String url,
			JsonHttpResponseHandler responseHandler)
public static void put(String url, String payload,
			JsonHttpResponseHandler responseHandler)
```
See [Enginio](https://engin.io/) documentation for more details.
