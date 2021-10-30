package io.mosip.print.listener.service.impl;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import io.mosip.print.listener.model.EventModel;
import io.mosip.print.listener.service.ClientService;
import io.mosip.print.listener.util.ApplicationContext;
import io.mosip.print.listener.util.CryptoCoreUtil;
import io.mosip.print.listener.util.RestApiClient;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private RestApiClient restApiClient;

	@Autowired
	private CryptoCoreUtil cryptoCoreUtil;

	@Autowired
	private ApplicationContext  applicationContext;

	@Override
	public void generateCard(EventModel eventModel) {

		try {
			String dataShareUrl = eventModel.getEvent().getDataShareUri();
			URI dataShareUri = URI.create(dataShareUrl);
			String credentials = restApiClient.getApi(dataShareUri, String.class);
			String decryptedData = cryptoCoreUtil.decrypt(credentials);
			File pdfFile = new File(applicationContext.getPartnerResourCeBundle().getString("partner.pdf.download.path") + eventModel.getEvent().getId() + ".pdf");
			OutputStream os = new FileOutputStream(pdfFile);
			os.write(Base64.decodeBase64(decryptedData));
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}