package io.qtc.enginio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

public class EnginioHttpResponseHandler extends JsonHttpResponseHandler{
	Class<?> objectClass;
	Enginio eds;
	
	public EnginioHttpResponseHandler(Enginio eds) {
		super(DEFAULT_CHARSET);
		objectClass = EnginioObject.class;
		this.eds = eds;
	}
	
	public EnginioHttpResponseHandler(Enginio enginio, Class<?> objectClass) {
		this(enginio);
		this.objectClass = objectClass;
	}
	
	/**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, Object response) {

    }

    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, ArrayList<Object> response) {

    }
    
    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode) {
    	
    }
    
	@Override
	public void onSuccess(int statusCode, JSONObject response) {		
		Log.i("Enginio", response.toString());
		try {
			if(response.has("results")) {
				onSuccess(statusCode, response.getJSONArray("results"));
			}
			else {
				Log.i("Enginio", "onSuccess");
				Object object = parseEnginioObject(response);
				onSuccess(statusCode, object);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onFailure(statusCode, e, response);
		}
		
	}
	
	@Override
	public void onSuccess(int statusCode, JSONArray response) {
		try {
			ArrayList<Object> objects = new ArrayList<Object>(response.length());
			for(int i= 0; i < response.length(); i++) { 
				Object object = parseEnginioObject(response.getJSONObject(i)); 
				objects.add(object);
			}
			onSuccess(statusCode, objects);
		} catch (JSONException e) {
			onFailure(statusCode, e, response);
		}
		
	}
	
	private String parseObjectType(String objectType) {
		return objectType.replaceFirst("objects.", "");
	}
	
	private Object parseEnginioObject(JSONObject response) throws JSONException {
		String objectType = "";
		if(response.has("objectType")) {
			 objectType = parseObjectType(response.getString("objectType"));
		}
		
		Object object;
		
		try {
			Constructor<?> cons = objectClass.getConstructor(Enginio.class);
			object = cons.newInstance(this.eds);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			object = new EnginioObject(this.eds);
		} catch (InstantiationException e) {
			e.printStackTrace();
			object = new EnginioObject(this.eds);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			object = new EnginioObject(this.eds);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			object = new EnginioObject(this.eds);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			object = new EnginioObject(this.eds);
		}
		
		((EnginioObject)object).setObjectType(objectType);
		((EnginioObject)object).setAttributes(response);
		return object;
	}
}
