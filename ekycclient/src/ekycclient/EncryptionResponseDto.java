package ekycclient;


public class EncryptionResponseDto {

	String encryptedIdentity ;
	String encryptedSessionKey;
	String requestHMAC;

	
	public String getEncryptedIdentity() {
		return encryptedIdentity;
	}
	public String getEncryptedSessionKey() {
		return encryptedSessionKey;
	}
	public String getRequestHMAC() {
		return requestHMAC;
	}
	public void setEncryptedIdentity(String value) {
		encryptedIdentity = value;

	}

	public void setEncryptedSessionKey(String value) {
		encryptedSessionKey = value;
		
	}

	public void setRequestHMAC(String value) {
		requestHMAC = value;
		
	}


}
