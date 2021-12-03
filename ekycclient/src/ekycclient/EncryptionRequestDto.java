package ekycclient;

import java.util.HashMap;

public class EncryptionRequestDto {

	HashMap<String, Object > identityRequest;
	
	public void setIdentityRequest(HashMap<String,Object> value) {
		identityRequest  = value;
	}
	public HashMap<String, Object> getIdentityRequest(){
		return identityRequest;
	}

}
