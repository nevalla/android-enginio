package io.qtc.enginio;

public class EnginioUser extends EnginioObject {

	public EnginioUser(Enginio eds) {
		super(eds);
	}
	
	@Override
	protected String getObjectTypeUrl() {
		return "users/";
	}
}
