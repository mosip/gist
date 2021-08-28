package io.mosip.partner.partnermanagement.constant;


public enum LoggerFileConstant {

	SUCCESS("SUCCESS", "SUCCESS"),
	SESSIONID("SESSIONID", "SESSIONID"),
	APPLICATIONID("APPLICATIONID", "PARTNER_MANAGER");


	private final String code;
	private final String message;

	LoggerFileConstant(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	public String toString() {
		return message;
	}
}
