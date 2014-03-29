package io.qtc.enginio;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class Enginio {
	private static final String BASE_URL = "https://api.engin.io/v1/";

	private String backend_id;
	private String access_token;
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public Enginio(String backend_id) {
		super();
		client.setUserAgent("qtc-sdk-android/1.0");
		this.backend_id = backend_id;
	}

	public EnginioCollection getCollection(String name) {
		return new EnginioCollection(this, name, false);
	}
	
	public void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		setHeaders();
		if(params != null) {
		Log.i("RestClient", params.toString());
		}
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public void post(String url, String payload,
			JsonHttpResponseHandler responseHandler) {
		setHeaders();
		StringEntity entity;
		try {
			entity = new StringEntity(payload);
			client.post(null, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			responseHandler.onFailure(e.getMessage(), e);
		}
		
	}
	
	public void delete(String url,
			JsonHttpResponseHandler responseHandler) {
		setHeaders();
		client.delete(getAbsoluteUrl(url), responseHandler);
	}
	
	public void put(String url, String payload,
			JsonHttpResponseHandler responseHandler) {
		StringEntity entity;
		setHeaders();
		try {
			entity = new StringEntity(payload);
			client.put(null, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		} catch (UnsupportedEncodingException e) {
			responseHandler.onFailure(e.getMessage(), e);
		}
		
	}

	public void login(String username, String password, final JsonHttpResponseHandler responseHandler) {
		setHeaders("application/x-www-form-urlencoded");
		
		RequestParams params = new RequestParams();
		params.add("grant_type", "password");
		params.add("username", username);
		params.add("password", password);
		
		Log.i("Login", "post "+getAbsoluteUrl("auth/oauth2/token"));
		client.post(getAbsoluteUrl("auth/oauth2/token"), params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					access_token = response.getString("access_token");
					responseHandler.onSuccess(response);
				} catch (JSONException e) {
					responseHandler.onFailure(statusCode, e, response);
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable e, JSONObject errorResponse) {
				responseHandler.onFailure(statusCode, e, errorResponse);
			}
		});
		
	}
	
	

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
	
	private void setHeaders() {
		client.addHeader("Enginio-Backend-Id", this.backend_id);
		client.addHeader("Content-Type", "application/json");
		client.addHeader("Accept", "application/json");
		if(this.access_token != null) {
			client.addHeader("Authorization", "BEARER "+this.access_token);
		}
		
	}
	
	private void setHeaders(String accept) {
		setHeaders();
		client.removeHeader("Accept");
		client.addHeader("Accept", accept);
		
	}
}