package io.qtc.enginio;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONValue;

public class EnginioObject {
	
	public String objectType;
	
	private Enginio eds;
	private JSONObject attributes;

	public EnginioObject(Enginio eds) {
		this.eds = eds;
		this.attributes = new JSONObject();
		
	}
	
	public EnginioObject(Enginio eds, String objectType) {
		this(eds);
		this.objectType = objectType;
	}
	
	public void setAttributes(JSONObject object) {
		this.attributes = object;
	}
	
	public String getOjectType() {
		return this.objectType;
	}
	
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	public void put(String key, double value) {
		try {
			this.attributes.put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public void put(String key, boolean value) {
		try {
			this.attributes.put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public void put(String key, Object value) {
		try {
			this.attributes.put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public void put(String key, int value) {
		try {
			this.attributes.put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public void put(String key, String value) {
		try {
			this.attributes.put(key, value);
		} catch (JSONException e) {
			
		}
	}
	
	public String getString(String key) {
		String result = null;
		try {
			result = this.attributes.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean getBoolean(String key, boolean defaultValue) {
		boolean result = defaultValue;
		try {
			result = this.attributes.getBoolean(key);
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		return result;
	}
	
	public void save(EnginioHttpResponseHandler responseHandler ) {
		String jsonText = JSONValue.toJSONString(this.attributes);
		if(this.getString("id") != null) {
			this.eds.put(getObjectTypeUrl()+this.getString("id"), jsonText, getSimpleResponseHandler(responseHandler));
		}
		else {
			this.eds.post(this.getObjectTypeUrl(), jsonText, getSimpleResponseHandler(responseHandler));
		}
	}
	
	public void fetch(EnginioHttpResponseHandler responseHandler) {
		if(this.getString("id") != null) {
			this.eds.get(this.getObjectTypeUrl()+this.getString("id"), null, getSimpleResponseHandler(responseHandler));
		}
			
	}
	
	public void delete(EnginioHttpResponseHandler responseHandler) {
		this.eds.delete(this.getObjectTypeUrl()+this.getString("id"),  getSimpleResponseHandler(responseHandler));
	}
	

	public String toString() {
		return this.attributes.toString();
	}
	
	protected String getObjectTypeUrl() {
		return "objects/"+this.getOjectType()+"/";
	}
	
	
	protected EnginioObject getInstance() {
		return this;
	}
	
	private EnginioHttpResponseHandler getSimpleResponseHandler(final EnginioHttpResponseHandler responseHandler) {
		return new EnginioHttpResponseHandler(this.eds) {
			@Override
			public void onSuccess(int statusCode, Object response) {
				attributes = ((EnginioObject)response).attributes;
				responseHandler.onSuccess(statusCode, getInstance());
			}
			
			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONObject errorResponse) {
				
				responseHandler.onFailure(statusCode, e, errorResponse);
			}
			
		};
	}
	
}
