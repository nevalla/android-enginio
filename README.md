Qt Cloud Services SDK for Android
==============================

This is Qt Cloud Services SDK for Android. An asynchronous, callback-based Enginio client library is built on top of [Android Asynchronous Http Client](https://github.com/loopj/android-async-http) and Apache's [HttpClient](http://hc.apache.org/httpcomponents-client-ga/) libraries.

## What is Qt Cloud Services? See below:

* The Qt Cloud Services home page is at https://www.qtc.io
* The Developer Documentation page is at https://developer.qtc.io


## Getting Started

You can find a getting started guide for Qt Cloud Services at:

http://developer.qtc.io/qtc/getting-started?snippets=android

If you are looking for service specific guides, please see:

* [Enginio Data Storage](http://developer.qtc.io/eds/getting-started?snippets=android)


##Installation

**In ADT (Eclipse)**
Clone this repo and reference EnginioClient library in your project. [More details](http://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject).

## Usage

### High-level API
Overwrite EnginioObject class
```java
public class Todo extends EnginioObject {

	public Todo(Enginio eds) {
		super(eds, "todos");
	}
	
}
```

Use your class in code
```java
Enginio eds = new Enginio({EDS_BACKEND_ID});
EnginioCollection todos = eds.getCollection("todos");

// find todos objects
todos.find(new EnginioHttpResponseHandler(eds, Todo.class){
	@Override
	public void onSuccess(int statusCode, ArrayList<Object> response) {
		// handle UI changes etc
	}
	@Override
	public void onFailure(int statusCode, Throwable e,
			JSONObject errorResponse) {
		Log.i("Enginio failure", errorResponse.toString());
	}
});
    	
// create a new todo object
Todo todo = new Todo(eds); 
todo.put("title", "My first todo");
todo.save(new EnginioHttpResponseHandler(todos) {
  	@Override
  	public void onSuccess(int statusCode,
  			Object response) {
    		// handle UI changes etc
    	}
});
```

### Low-level REST API
Use the following Enginio's public REST methods. See [Android Asynchronous Http Client](https://github.com/loopj/android-async-http) for more details.
```
public void get(String url, RequestParams params,
			JsonHttpResponseHandler responseHandler)
public void post(String url, String payload,
			JsonHttpResponseHandler responseHandler)
public void put(String url, String payload,
			JsonHttpResponseHandler responseHandler)
public void delete(String url,
			JsonHttpResponseHandler responseHandler)
```

## Contributing

1. Fork it ( http://github.com/nevalla/qtc-sdk-android/fork )
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
