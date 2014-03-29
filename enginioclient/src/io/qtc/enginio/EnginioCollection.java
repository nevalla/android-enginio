package io.qtc.enginio;

import com.loopj.android.http.RequestParams;

public class EnginioCollection {

	Enginio eds;
	String name;
	String namePath;
	boolean useInternals = false;
	
	
	public EnginioCollection(Enginio eds, String name,
			boolean useInternals) {
		super();
		this.eds = eds;
		this.name = name;
		this.useInternals = useInternals;
		this.initializeNamePath();
		
	}
	
	private void initializeNamePath() {
		// construct address to namePath
	    String tmpPath = name.toLowerCase();
	    String[] parts = tmpPath.split("\\.");
	    if(parts.length > 1){
	        // the name is supplied as dotted notation; verify
	        if((parts[0] == "objects") && (parts[1].length() > 0)){
	            this.namePath = "objects/"+parts[1]+"/";
	        }
	    } else {
	        // simple notation; assume all collections are custom collections (belong to objects.*)
	        this.namePath = "objects/" + tmpPath + "/";

	        // override users and usergroups if useInternals
	        if(useInternals){
	            if(tmpPath == "users"){
	                this.namePath = "users/";
	            } else if(tmpPath == "usergroups"){
	                this.namePath = "usergroups/";
	            }
	        }
	    }
	}
	
	public void insert(String payload, EnginioHttpResponseHandler responseHandler) {
		this.eds.post(this.namePath, payload, responseHandler);
	}
	
	public void update(String objId, String payload, EnginioHttpResponseHandler responseHandler) {
		this.eds.put(this.namePath+objId, payload, responseHandler);
	}
	
	public void delete(String objId, EnginioHttpResponseHandler responseHandler) {
		this.eds.delete(this.namePath+objId, responseHandler);
	}
	
	public void find(EnginioHttpResponseHandler responseHandler) {
		this.eds.get(this.namePath, null, responseHandler);
	}
	
	public void find(String q, int limit, int offset, String sort, boolean count, String include, EnginioHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		if(q != null) {
			params.add("q",q);
		}
		if(limit > 0) {
			params.add("limit", Integer.toString(limit));
		}
		if(offset > -1) {
			params.add("offset", Integer.toString(offset));
		}
		if(sort != null) {
			params.add("sort", sort);
		}
		if(count) {
			params.add("count", "1");
		}
		if(include != null) {
			params.add("include", include);
		}
		this.eds.get(this.namePath, params, responseHandler);
	}
	
	public void findOne(String objId, EnginioHttpResponseHandler responseHandler) {
		this.eds.get(this.namePath+objId, null, responseHandler);
	}
}
