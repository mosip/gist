package io.mosip.print.listener.service.impl;

import java.awt.print.PrinterJob;
import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.activemq.ActiveMQListener;
import io.mosip.print.listener.constant.LoggerFileConstant;
import io.mosip.print.listener.constant.PrintTransactionStatus;
import io.mosip.print.listener.dto.MQResponseDto;
import io.mosip.print.listener.dto.PrintStatusRequestDto;
import io.mosip.print.listener.exception.PlatformErrorMessages;
import io.mosip.print.listener.logger.PrintListenerLogger;
import io.mosip.print.listener.model.EventModel;
import io.mosip.print.listener.service.ClientService;
import io.mosip.print.listener.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.*;

@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	private RestApiClient restApiClient;

	@Autowired
	private CryptoCoreUtil cryptoCoreUtil;

	@Autowired
	private ApplicationContext  applicationContext;

	@Autowired
	private ActiveMQListener activeMQListener;

	@Autowired
	private PrinterUtil printerUtil;

	/** The print logger. */
	Logger clientLogger = PrintListenerLogger.getLogger(ClientServiceImpl.class);

	@Override
	public void generateCard(EventModel eventModel) {

		try {
			String dataShareUrl = eventModel.getEvent().getDataShareUri();
			URI dataShareUri = URI.create(dataShareUrl);
			String credentials = restApiClient.getApi(dataShareUri, String.class);
			String decryptedData = cryptoCoreUtil.decrypt(credentials);
			String filePath = applicationContext.getPartnerResourCeBundle().getString("partner.pdf.download.path");
			if(!filePath.endsWith("/"))
				filePath = filePath + "/";

			File pdfFile = new File( filePath + eventModel.getEvent().getId() + ".pdf");
			OutputStream os = new FileOutputStream(pdfFile);
			os.write(Base64.decodeBase64(decryptedData));
			os.close();

			if (printerUtil.initiatePrint(pdfFile.getName())) {
				PrintStatusRequestDto printStatusRequestDto = new PrintStatusRequestDto();
				printStatusRequestDto.setPrintStatus(PrintTransactionStatus.PRINTED);
				printStatusRequestDto.setProcessedTime(DateUtils.getUTCCurrentDateTimeString());
				printStatusRequestDto.setId(eventModel.getEvent().getPrintId());
				MQResponseDto mqResponseDto = new MQResponseDto("mosip.print.pdf.response", printStatusRequestDto);
				ResponseEntity<Object> mqResponse = new ResponseEntity<Object>(mqResponseDto, HttpStatus.OK);
				activeMQListener.sendToQueue(mqResponse, 1);
			} else {
				clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
						LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed",
								PlatformErrorMessages.PRT_FAILED.name());
				throw new Exception(PlatformErrorMessages.PRT_FAILED.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
			clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR : " + e.getMessage());
			clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));

			try {
				PrintStatusRequestDto printStatusRequestDto = new PrintStatusRequestDto();
				printStatusRequestDto.setPrintStatus(PrintTransactionStatus.ERROR);
				printStatusRequestDto.setProcessedTime(DateUtils.getUTCCurrentDateTimeString());
				printStatusRequestDto.setId(eventModel.getEvent().getPrintId());
				printStatusRequestDto.setStatusComments(e.getMessage());
				MQResponseDto mqResponseDto = new MQResponseDto("mosip.print.pdf.response", printStatusRequestDto);
				ResponseEntity<Object> mqResponse = new ResponseEntity<Object>(mqResponseDto, HttpStatus.OK);

				activeMQListener.sendToQueue(mqResponse, 1);
			} catch (Exception ex) {
				clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR : " + e.getMessage());
				clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
				ex.printStackTrace();
			}
		}
	}
}