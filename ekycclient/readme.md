
						eKYC Client
						----------------
	
	This sample code shows how to build and make client request for otp based ekyc auth request
	This assumes you have already onbordeed the  ekyc partner ( Auth Partner type) and  also got MISP License key
	We need Partner ID, Partner Key and MISP License key
	Update these details into application.properties file
		
	
	Please serup the following in certfolder
	1.partnerid-partner.p12
		This keystore should contain the private key of the partner and MOSIP signed certificate
		the file name should be based on partnerid-partner
		For example, if partner id is partner100 then file name would be 
			partner100-partner.p12
		You will get The signed partner certificate when you on boarded the partner 
		You need to put it into the above keystore file. You may use the tool keystore explorer which is a handy GUI tool
	
	2. ida-partner.cer
		This certificate, one could download from your MOSIP platform using the API -
		POST https://{baseurl}/idauthentication/v1/internal/getCertificate?applicationId=IDA&referenceId=PARTNER
		
	3. Keystore password and alias names
		Please note down these two properties while you were creating the above partner keystore and update the application.properties file
	
	4. application.properties reference
		Following properties are required to be filled in 
	
		4.1 urlBase= baseurl
		For example, your APIs are exposed via https://yourcompany.mosip.net then set 
			urlBase=https://yourcompany.mosip.net/

		4.2 idtype
			idtype=UIN 
			This could be either UIN or VID
			
		4.3
			uid= xxxxxx
			If you have set idtype as UIN, please set the UIN value for this property
			If you have set idtype as VID, then this should contain a virtual id of the resident
		4.4
			partnerId=xxxxxx
			Plese enter the Partner ID here
		4.5
			mispLicKey=xxxxxx
			Please enter MISP license key in this attribute
			
		4.6
			partnerKey=xxxxxx
			
			Please enter Partner Key in this attribute
		4.7	
			clientid=xxxx
			clientsecret=xxxx
			appid=xxxxxx
			
			These attributes should have been configured in keycloack for API access
			
		4.8
			keysfolder=certfolder
			
			Folder path ( relative or absolute ) where the above keystore and certificate files are kept
			
		4.9
			certpassword=
			certalias=

			Please fill these attributes for the keystore
			
		4.10
			notificationChannel=EMAIL
			
			The possible values are -
				EMAIL
				OTP
				EMAIL,OTP
				
