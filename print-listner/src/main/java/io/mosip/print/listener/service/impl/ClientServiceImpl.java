package io.mosip.print.listener.service.impl;

import java.awt.print.PrinterJob;
import java.io.*;
import java.net.InetAddress;
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

	@Autowired
	private Environment env;

	/** The print logger. */
	Logger clientLogger = PrintListenerLogger.getLogger(ClientServiceImpl.class);

	@Override
	public void generateCard(EventModel eventModel) {

		try {
			System.out.println("Processing Message RID :  " + eventModel.getEvent().getId());
			String dataShareUrl = eventModel.getEvent().getDataShareUri();
			dataShareUrl = dataShareUrl.replace("http://", "https://");
			URI dataShareUri = URI.create(dataShareUrl);
			String credentials = restApiClient.getApi(dataShareUri, String.class);
			System.out.println("Data Decryption Started RID :  " + eventModel.getEvent().getId());
			String decryptedData = cryptoCoreUtil.decrypt(credentials);
			System.out.println("Data Decryption Completed RID :  " + eventModel.getEvent().getId());
			String filePath = env.getProperty("partner.pdf.download.path");
			if(!printerUtil.isPrintArchievePathExist()) {
				InetAddress address = InetAddress.getLocalHost();
				System.out.println("PDF Archieve Path not exist. Path : " + filePath + ", RID : " + eventModel.getEvent().getId());

				clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
						LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed",
						PlatformErrorMessages.PRT_PATH_NOT_FOUND.name() + "Machine Id : " + address.getHostName() + ", RID : " + eventModel.getEvent().getId());
				throw new Exception(PlatformErrorMessages.PRT_PATH_NOT_FOUND.getMessage() + "Machine Id : " + address.getHostName() + ", RID : " + eventModel.getEvent().getId());
			}

			if(!filePath.endsWith("/"))
				filePath = filePath + "/";

			File pdfFile = new File( filePath + eventModel.getEvent().getId() + ".pdf");
			OutputStream os = new FileOutputStream(pdfFile);
			os.write(Base64.decodeBase64(decryptedData));
			os.close();

			boolean printRequired  = env.getProperty("mosip.print.pdf.printing.required", boolean.class);
			System.out.println("Print Required :  " + printRequired);

			if(printRequired) {
				System.out.println("Print Started RID :  " + eventModel.getEvent().getId());

				if (printerUtil.initiatePrint(pdfFile.getName())) {
					System.out.println("Print Completed Successfully RID :  " + eventModel.getEvent().getId());
					PrintStatusRequestDto printStatusRequestDto = new PrintStatusRequestDto();
					printStatusRequestDto.setPrintStatus(PrintTransactionStatus.PRINTED);
					printStatusRequestDto.setProcessedTime(DateUtils.getUTCCurrentDateTimeString());
					printStatusRequestDto.setId(eventModel.getEvent().getPrintId());
					MQResponseDto mqResponseDto = new MQResponseDto("mosip.print.pdf.response", printStatusRequestDto);
					ResponseEntity<Object> mqResponse = new ResponseEntity<Object>(mqResponseDto, HttpStatus.OK);

					String[] args = new String[]{eventModel.getEvent().getPrintId(),
							"'" + eventModel.getEvent().getId(),
													PrintTransactionStatus.PRINTED.toString()};
					CSVLogWriter.setLogMap(eventModel.getEvent().getPrintId(), args);
					activeMQListener.sendToQueue(mqResponse, 1);
				} else {
					System.out.println("Print Failed RID :  " + eventModel.getEvent().getId());

					clientLogger.error(LoggerFileConstant.SESSIONID.toString(),
							LoggerFileConstant.REGISTRATIONID.toString(), "Print Failed",
							PlatformErrorMessages.PRT_FAILED.name());
					throw new Exception(PlatformErrorMessages.PRT_FAILED.getMessage());
				}
			} else {
				System.out.println("Print Saved Locally Successfully RID :  " + eventModel.getEvent().getId());
				PrintStatusRequestDto printStatusRequestDto = new PrintStatusRequestDto();
				printStatusRequestDto.setPrintStatus(PrintTransactionStatus.SAVED_IN_LOCAL);
				printStatusRequestDto.setProcessedTime(DateUtils.getUTCCurrentDateTimeString());
				printStatusRequestDto.setId(eventModel.getEvent().getPrintId());
				MQResponseDto mqResponseDto = new MQResponseDto("mosip.print.pdf.response", printStatusRequestDto);
				ResponseEntity<Object> mqResponse = new ResponseEntity<Object>(mqResponseDto, HttpStatus.OK);

				String[] args = new String[]{eventModel.getEvent().getPrintId(),
						"'" + eventModel.getEvent().getId(),
						PrintTransactionStatus.SAVED_IN_LOCAL.toString()};
				CSVLogWriter.setLogMap(eventModel.getEvent().getPrintId(), args);
				activeMQListener.sendToQueue(mqResponse, 1);
			}
			CSVLogWriter.writePrintStatus();
		} catch (Exception e) {
			System.out.println("Error :  " + e.getMessage());
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

				String[] args = new String[]{eventModel.getEvent().getPrintId(),
						"'" + eventModel.getEvent().getId(),
						PrintTransactionStatus.ERROR.toString()};
				CSVLogWriter.setLogMap(eventModel.getEvent().getPrintId(), args);
				CSVLogWriter.writePrintStatus();

				activeMQListener.sendToQueue(mqResponse, 1);
			} catch (Exception ex) {
				clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR : " + e.getMessage());
				clientLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ClientServiceImpl","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
				CSVLogWriter.writePrintStatus();
			}
		}
	}
}