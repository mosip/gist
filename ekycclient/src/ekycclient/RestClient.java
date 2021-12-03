package ekycclient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.Header;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class RestClient {

	static String _urlBase = "https://sandbox.mosip.net";
	static String _token = null;
	static String _userId ;
	static String _userPasswd;
	static String _appId;
	
	public static void setConfiguration(String userName, String passwd, String appId) {
		_userId = userName;
		_userPasswd = passwd;
		_appId = appId;
		
	}
	public static void setUrlBase(String urlBase) {
		_urlBase = urlBase;
	}
	public static String getUrlBase() {
		return _urlBase;
	}
	
	public static String authenticate() throws Exception {
		OkHttpClient client =  new OkHttpClient();
		String strFmt = "{ \"id\": \"mosip.authentication.useridPwd\",  \"metadata\": {},  \"request\": { \"appId\": \"%s\", \"password\": \"%s\", \"userName\": \"%s\" }, \"requesttime\": \"%s\", \"version\": \"1.0\"}";
	
		String strBody = String.format(strFmt, _appId,_userPasswd,_userId,getUTCDateTime( null));
		
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strBody);
		Request request = new Request.Builder()
				  .url(_urlBase +"/v1/authmanager/authenticate/useridPwd")
				  .method("POST", body)
//				  .addHeader("Authorization", "Bearer eyJhbGciOiJFRDI1NTE5In0.eyJzdWIiOiJiZDRmMzg0NzBhMDRkNDU1YzdjNiIsImlhdCI6MTYwMTQ3Nzc4OX0.dHwX_D3xJfTJVoD2QWjYxL1sp8T0ufsrX3Puv0BxYKvFipwYBKxem9U0lqYh2Bg8ObviMVc30uvzCb-sQc41DA")
				  .addHeader("Content-Type", "application/json")
				  //.addHeader("Cookie", "Authorization=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJfT3pmTkgxVnRMNFN6RmxySGFMSEx4dmVaTUxQaUR1U20tX2g2SzdJRFFzIn0.eyJqdGkiOiJhMTdkMGU0Yi1hMGYyLTRlNmMtYWJjMC1jOWJkNDRmYmU2NzQiLCJleHAiOjE2MzEyMjI1MjAsIm5iZiI6MCwiaWF0IjoxNjMxMTg2NTIwLCJpc3MiOiJodHRwczovL3NhbmRib3gubW9zaXAubmV0L2tleWNsb2FrL2F1dGgvcmVhbG1zL21vc2lwIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImEzNzNmNTAzLTI2OWQtNDJjYS1hYjI3LTRlODViZTk3YjA4YSIsInR5cCI6IkJlYXJlciIsImF6cCI6Im1vc2lwLWFkbWluLWNsaWVudCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6ImM0ZGFlYjM1LWQxY2QtNDI0OS05YmEwLWU0YmYyNmNhNmY2MiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9zYW5kYm94Lm1vc2lwLm5ldCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUEFSVE5FUiIsIlJFR0lTVFJBVElPTl9PRkZJQ0VSIiwiUkVHSVNUUkFUSU9OX09QRVJBVE9SIiwiTUVUQURBVEFfUkVBRCIsIlJFR0lTVFJBVElPTl9QUk9DRVNTT1IiLCJSRUdJU1RSQVRJT05fQURNSU4iLCJQUkVfUkVHSVNUUkFUSU9OIiwiWk9OQUxfQURNSU4iLCJSRUdJU1RSQVRJT05fU1VQRVJWSVNPUiIsIlBSRVJFRyIsIm9mZmxpbmVfYWNjZXNzIiwiUEFSVE5FUl9BRE1JTiIsInVtYV9hdXRob3JpemF0aW9uIiwiR0xPQkFMX0FETUlOIiwiUFJFX1JFR0lTVFJBVElPTl9BRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6Ik5pa2hpbGVzaCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiMTEwMTI2IiwiZ2l2ZW5fbmFtZSI6Ik5pa2hpbGVzaCIsImZhbWlseV9uYW1lIjoiVGVzdCIsImVtYWlsIjoiMTEwMTI2QHh5ei5jb20ifQ.rFYb0IvJcom96C2dqBfpIV6D8KGJDwUBdeXVK9Jl_xzikJYZX61gqr7mvtqGbiwjMWxw5BohbJDdnkMf4W8EoHQxJWjyYj86elEPs4ZL87qc3WTSZ9LNfdP6EjVBqn0X_FxZXUqeEWXk4QsKfFOoH7Ai8Jdc_VQ6MlsBimlley6uCsV7sm9FxK7xg88ZNmwUIGiM4YSHFUGGETK7rzNNJiuIV-suCQpraROoHyJWOQeNoEIpm9opPMGEfyKGR7uCkWeMOmd7nXj9C_-p1KgIz9NOpUPvCSb22YGC8pzfSE6Quw1euZ374tuyjbasASD2h0hUKuwOiHI2iNG77FI16A")
				  .build();
		Response response = client.newCall(request).execute();
		System.out.println("code=" + response.code());
		if(response.code() == 200) {
			_token = response.header("Authorization");
			response.body().close();
			return _token;
			
		}
		throw new Exception("Invalid user/password");
	}
	public static String authenticateSecret() throws Exception {
		OkHttpClient client =  new OkHttpClient();
		String strFmt = "{ \"id\": \"string\",  \"metadata\": {},  \"request\": { \"appId\": \"%s\", \"secretKey\": \"%s\", \"clientId\": \"%s\" }, \"requesttime\": \"%s\", \"version\": \"v1\"}";
	
		String strBody = String.format(strFmt, _appId,_userPasswd,_userId,getUTCDateTime( null));
		
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strBody);
	
		Request request = new Request.Builder()
				  .url(_urlBase +"/v1/authmanager/authenticate/clientidsecretkey")
				  .method("POST", body)
//				  .addHeader("Authorization", "Bearer eyJhbGciOiJFRDI1NTE5In0.eyJzdWIiOiJiZDRmMzg0NzBhMDRkNDU1YzdjNiIsImlhdCI6MTYwMTQ3Nzc4OX0.dHwX_D3xJfTJVoD2QWjYxL1sp8T0ufsrX3Puv0BxYKvFipwYBKxem9U0lqYh2Bg8ObviMVc30uvzCb-sQc41DA")
				  .addHeader("Content-Type", "application/json")
				  //.addHeader("Cookie", "Authorization=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJfT3pmTkgxVnRMNFN6RmxySGFMSEx4dmVaTUxQaUR1U20tX2g2SzdJRFFzIn0.eyJqdGkiOiJhMTdkMGU0Yi1hMGYyLTRlNmMtYWJjMC1jOWJkNDRmYmU2NzQiLCJleHAiOjE2MzEyMjI1MjAsIm5iZiI6MCwiaWF0IjoxNjMxMTg2NTIwLCJpc3MiOiJodHRwczovL3NhbmRib3gubW9zaXAubmV0L2tleWNsb2FrL2F1dGgvcmVhbG1zL21vc2lwIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImEzNzNmNTAzLTI2OWQtNDJjYS1hYjI3LTRlODViZTk3YjA4YSIsInR5cCI6IkJlYXJlciIsImF6cCI6Im1vc2lwLWFkbWluLWNsaWVudCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6ImM0ZGFlYjM1LWQxY2QtNDI0OS05YmEwLWU0YmYyNmNhNmY2MiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cHM6Ly9zYW5kYm94Lm1vc2lwLm5ldCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUEFSVE5FUiIsIlJFR0lTVFJBVElPTl9PRkZJQ0VSIiwiUkVHSVNUUkFUSU9OX09QRVJBVE9SIiwiTUVUQURBVEFfUkVBRCIsIlJFR0lTVFJBVElPTl9QUk9DRVNTT1IiLCJSRUdJU1RSQVRJT05fQURNSU4iLCJQUkVfUkVHSVNUUkFUSU9OIiwiWk9OQUxfQURNSU4iLCJSRUdJU1RSQVRJT05fU1VQRVJWSVNPUiIsIlBSRVJFRyIsIm9mZmxpbmVfYWNjZXNzIiwiUEFSVE5FUl9BRE1JTiIsInVtYV9hdXRob3JpemF0aW9uIiwiR0xPQkFMX0FETUlOIiwiUFJFX1JFR0lTVFJBVElPTl9BRE1JTiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6Ik5pa2hpbGVzaCBUZXN0IiwicHJlZmVycmVkX3VzZXJuYW1lIjoiMTEwMTI2IiwiZ2l2ZW5fbmFtZSI6Ik5pa2hpbGVzaCIsImZhbWlseV9uYW1lIjoiVGVzdCIsImVtYWlsIjoiMTEwMTI2QHh5ei5jb20ifQ.rFYb0IvJcom96C2dqBfpIV6D8KGJDwUBdeXVK9Jl_xzikJYZX61gqr7mvtqGbiwjMWxw5BohbJDdnkMf4W8EoHQxJWjyYj86elEPs4ZL87qc3WTSZ9LNfdP6EjVBqn0X_FxZXUqeEWXk4QsKfFOoH7Ai8Jdc_VQ6MlsBimlley6uCsV7sm9FxK7xg88ZNmwUIGiM4YSHFUGGETK7rzNNJiuIV-suCQpraROoHyJWOQeNoEIpm9opPMGEfyKGR7uCkWeMOmd7nXj9C_-p1KgIz9NOpUPvCSb22YGC8pzfSE6Quw1euZ374tuyjbasASD2h0hUKuwOiHI2iNG77FI16A")
				  .build();
		Response response = client.newCall(request).execute();
		System.out.println("code=" + response.code());
		if(response.code() == 200) {
			/*
			System.out.println( response.body().string());
			List<String> Cookielist = response.headers().values("set-cookie");
			String jsessionid = (Cookielist .get(0).split(";"))[0];
			System.out.println(Cookielist.get(0));
			
			Headers headerList = response.headers();


			for(String n: headerList.names()) {
                System.out.println(n +"=" + headerList.get(n));
			}*/
			_token = response.header("authorization");// "Set-Cookie");//"Authorization");
			System.out.println(_token);
			response.body().close();
			return _token;
			
		}
		throw new Exception("Invalid user/password");
	}

	public static String getUTCDateTime(LocalDateTime time) {
		String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATEFORMAT);
        if (time == null){
            time = LocalDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());
        }  
		String utcTime = time.format(dateFormat);
		return utcTime;
    }

	public static String sendOtpRequest(String token, String strReq, String strSignature, String mispLicenceKey, String partnerId, String partnerAPIKey) throws IOException {
		String retVal = null;
		
		OkHttpClient client = new OkHttpClient();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strReq);
		System.out.println(_urlBase+ "/idauthentication/v1/otp/" + mispLicenceKey+"/"+ partnerId + "/" + partnerAPIKey);
		Request request = new Request.Builder()
				  .url(_urlBase+ "/idauthentication/v1/otp/" + mispLicenceKey+"/"+ partnerId + "/" + partnerAPIKey)
				  .method("POST", body)
				  .addHeader("Signature",strSignature) 
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Cookie","Authorization="+ token) 
				  .addHeader("Authorization","Bearer "+ token) 
				  
				  .build();
		Response response = client.newCall(request).execute();
				
				
		if(response.code() == 200) {
			retVal =  new String(response.body().bytes());
		}
		response.body().close();
		return retVal;
	}
	public static String sendOtpKYCRequest(String token, String strReq, String strSignature, String mispLicenceKey, String partnerId, String partnerAPIKey) throws IOException {
		String retVal = null;
		
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strReq);
		System.out.println(strReq);
		System.out.println(_urlBase+ "/idauthentication/v1/kyc/" + mispLicenceKey+"/"+ partnerId + "/" + partnerAPIKey);
		
		Request request = new Request.Builder()
				  .url(_urlBase+ "/idauthentication/v1/kyc/" + mispLicenceKey+"/"+ partnerId + "/" + partnerAPIKey)
				  .method("POST", body)
				  .addHeader("Signature",strSignature) 
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Cookie","Authorization="+ token) 
				  .addHeader("Authorization","Bearer "+ token)   
				  
				  .build();
		
		Response response = client.newCall(request).execute();
						
		if(response.code() == 200) {
			retVal =  new String(response.body().bytes());
			System.out.println("Response 200OK:" + retVal);
		}
		response.body().close();
		return retVal;
	}
	public static String getCertificate() throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				  .url(_urlBase+ "/idauthentication/v1/internal/getCertificate?applicationId=IDA&referenceId=PARTNER")
				  .method("GET", null)
				  .addHeader("Cookie","Authorization="+ _token) 
				  .addHeader("Authorization","Bearer "+ _token)
				  .build();
		Response response = client.newCall(request).execute();
		if(response.code() == 200) {
			return new String(response.body().bytes());
		}
		return null;
	}
}
