package io.qtc.enginio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class EnginioQuery {

	private JSONObject criteria;
	
	public EnginioQuery() {
		criteria = new JSONObject();
		
	}
	
	public void add(String key, Object value) {
		this.criteria.put(key, value);
	}
	
	public void in(String key, List values) {
		this.criteria.put(key, convertToMap("$in", values));
	}
	
	public void or(List values) {
		this.criteria.put("$or", values);
	}
	
	private Map convertToMap(String key, Object values) {
		HashMap<String, Object> criterion = new HashMap<String, Object>();
		criterion.put(key, values);
		return criterion;
	}
	
	public String toString() {
		return this.criteria.toJSONString();
	}
}
