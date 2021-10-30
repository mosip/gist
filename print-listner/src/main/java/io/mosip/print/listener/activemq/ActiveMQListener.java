package io.mosip.print.listener.activemq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.constant.LoggerFileConstant;
import io.mosip.print.listener.constant.PrintTransactionStatus;
import io.mosip.print.listener.dto.MQResponseDto;
import io.mosip.print.listener.dto.PrintMQDetails;
import io.mosip.print.listener.controller.PrintListenerController;
import io.mosip.print.listener.dto.PrintStatusRequestDto;
import io.mosip.print.listener.exception.ExceptionUtils;
import io.mosip.print.listener.model.Event;
import io.mosip.print.listener.model.EventModel;
import io.mosip.print.listener.util.DateUtils;
import io.mosip.print.listener.util.Helpers;
import io.mosip.print.listener.util.LoggerFactory;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.bouncycastle.asn1.tsp.TimeStampReq;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

@Component
public class ActiveMQListener {

	private static final Logger logger = LoggerFactory.getLogger(ActiveMQListener.class);

	@Value("${config.server.file.storage.uri}")
	private String configServerFileStorageURL;

	@Value("${print.activemq.listener.json}")
	private String printActiveMQListenerJson;
	
	@Value("${print.activemq.response.delay}")
	private int delayResponse;

	/** The Constant PRINTMQ. */
	private static final String PRINTMQ = "printMQ";

	/** The Constant USERNAME. */
	private static final String USERNAME = "userName";

	/** The Constant PASSWORD. */
	private static final String PASSWORD = "password";

	/** The Constant BROKERURL. */
	private static final String BROKERURL = "brokerUrl";

	/** The Constant FAIL_OVER. */
	private static final String FAIL_OVER = "failover:(";

	/** The Constant RANDOMIZE_FALSE. */
	private static final String RANDOMIZE_FALSE = ")?randomize=false";

	/** The Constant TYPEOFQUEUE. */
	private static final String TYPEOFQUEUE = "typeOfQueue";

	/** The Constant INBOUNDQUEUENAME. */
	private static final String INBOUNDQUEUENAME = "inboundQueueName";

	/** The Constant NAME. */
	private static final String NAME = "name";

	/** The Constant OUTBOUNDQUEUENAME. */
	private static final String OUTBOUNDQUEUENAME = "outboundQueueName";

	private ActiveMQConnectionFactory activeMQConnectionFactory;

	private static final String PRINT_PDF_DATA = "mosip.print.pdf.data";

//	private static final String ABIS_IDENTIFY = "mosip.abis.identify";

//	private static final String ABIS_DELETE = "mosip.abis.delete";

	private static final String ID = "id";

//	private static final String VALUE = "value";

	private Connection connection;
	private Session session;
	private Destination destination;

	/**
	 * This flag is added for development & debugging locally registration-processor-abis-sample.json
	 * If true then registration-processor-abis-sample.json will be picked from resources
	 */
	@Value("${local.development}")
	private boolean localDevelopment;

	public String outBoundQueue;

	@Autowired
	public PrintListenerController printListenerController;

	public void consumeLogic(javax.jms.Message message, String abismiddlewareaddress) {
		Integer textType = 0;
		String messageData = null;
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ",
				"Received message " + message);
		try {
			if (message instanceof TextMessage || message instanceof ActiveMQTextMessage) {
				textType = 1;
				TextMessage textMessage = (TextMessage) message;
				messageData = textMessage.getText();
			} else if (message instanceof ActiveMQBytesMessage) {
				textType = 2;
				messageData = new String(((ActiveMQBytesMessage) message).getContent().data);
			} else {
				logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","Received message is neither text nor byte");
				return ;
			}
			logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","Message Data " + messageData);
			Map map = new Gson().fromJson(messageData, Map.class);

			PrintStatusRequestDto printStatusRequestDto = new PrintStatusRequestDto();
			printStatusRequestDto.setPrintStatus(PrintTransactionStatus.SENT_FOR_PRINTING);
			printStatusRequestDto.setProcessedTime(DateUtils.getUTCCurrentDateTimeString());
			printStatusRequestDto.setId(map.get("printId").toString());
			MQResponseDto mqResponseDto = new MQResponseDto("mosip.print.pdf.response", printStatusRequestDto);
			ResponseEntity<Object> mqResponse = new ResponseEntity<Object>(mqResponseDto, HttpStatus.OK);
			sendToQueue(mqResponse, 1);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules();
			mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

			ResponseEntity<Object> obj = null;

			logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","go on sleep {} "+ delayResponse);
			TimeUnit.SECONDS.sleep(delayResponse);

			logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","Request type is " + map.get("id"));

			switch (map.get(ID).toString()) {
			case PRINT_PDF_DATA:
				Event event = new Event();
				event.setId(map.get("refId").toString());
				event.setDataShareUri(map.get("data").toString());
				event.setPrintId(map.get("printId").toString());
				EventModel eventModel = new EventModel();
				eventModel.setEvent(event);
				printListenerController.processDataShareUrl(eventModel);
				break;
			}
		} catch (Exception e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
	}

	public void sendToQueue(ResponseEntity<Object> obj, Integer textType) throws JsonProcessingException, UnsupportedEncodingException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","Response: " + obj.getBody().toString());
		if (textType == 2) {
			send(mapper.writeValueAsString(obj.getBody()).getBytes("UTF-8"),
					outBoundQueue);
		} else if (textType == 1) {
			send(mapper.writeValueAsString(obj.getBody()), outBoundQueue);
		}
	}

	public static String getJson(String configServerFileStorageURL, String uri, boolean localQueueConf) throws IOException, URISyntaxException {
		if (localQueueConf) {
			return Helpers.readFileFromResources("print-activemq-listener.json");
		} else {
			RestTemplate restTemplate = new RestTemplate();
			logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","Json URL " + configServerFileStorageURL + " : " + uri);
			return restTemplate.getForObject(configServerFileStorageURL + uri, String.class);
		}
	}

	public List<PrintMQDetails> getQueueDetails() throws IOException, URISyntaxException {
		List<PrintMQDetails> queueDetailsList = new ArrayList<>();

		String printQueueJsonStringValue = getJson(configServerFileStorageURL, printActiveMQListenerJson, localDevelopment);

		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ",printQueueJsonStringValue);
		JSONObject printQueueJson;
		PrintMQDetails queueDetail = new PrintMQDetails();
		Gson g = new Gson();

		try {
			printQueueJson = g.fromJson(printQueueJsonStringValue, JSONObject.class);

			ArrayList<Map> printQueueJsonArray = (ArrayList<Map>) printQueueJson.get(PRINTMQ);

			for (int i = 0; i < printQueueJsonArray.size(); i++) {

				Map<String, String> json = printQueueJsonArray.get(i);
				String userName = validateQueueJsonAndReturnValue(json, USERNAME);
				String password = validateQueueJsonAndReturnValue(json, PASSWORD);
				String brokerUrl = validateQueueJsonAndReturnValue(json, BROKERURL);
				String failOverBrokerUrl = FAIL_OVER + brokerUrl + "," + brokerUrl + RANDOMIZE_FALSE;
				String typeOfQueue = validateQueueJsonAndReturnValue(json, TYPEOFQUEUE);
				String inboundQueueName = validateQueueJsonAndReturnValue(json, INBOUNDQUEUENAME);
				String outboundQueueName = validateQueueJsonAndReturnValue(json, OUTBOUNDQUEUENAME);
				String queueName = validateQueueJsonAndReturnValue(json, NAME);

				this.activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);

				queueDetail.setTypeOfQueue(typeOfQueue);
				queueDetail.setInboundQueueName(inboundQueueName);
				queueDetail.setOutboundQueueName(outboundQueueName);
				queueDetail.setName(queueName);
				queueDetailsList.add(queueDetail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " +  e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
		}
		return queueDetailsList;
	}

	private String validateQueueJsonAndReturnValue(Map<String, String> jsonObject, String key) throws Exception {

		String value = (String) jsonObject.get(key);
		if (value == null) {
			throw new Exception("Value does not exists for key" + key);
		}
		return value;
	}

	public void setup() {
		try {
			if (connection == null || ((ActiveMQConnection) connection).isClosed()) {
				connection = activeMQConnectionFactory.createConnection();

				if (session == null) {
					connection.start();
					this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				}
			}
		} catch (JMSException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
	}

	public void runQueue() {
		try {
			List<PrintMQDetails> printQueueDetails = getQueueDetails();
			if (printQueueDetails != null && printQueueDetails.size() > 0) {

				for (int i = 0; i < printQueueDetails.size(); i++) {
					String outBoundAddress = printQueueDetails.get(i).getOutboundQueueName();
					outBoundQueue = outBoundAddress;
					QueueListener listener = new QueueListener() {

						@Override
						public void setListener(javax.jms.Message message) {
							consumeLogic(message, outBoundAddress);
						}
					};
					consume(printQueueDetails.get(i).getInboundQueueName(), listener,
							printQueueDetails.get(i).getTypeOfQueue());
				}

			} else {
				throw new Exception("Queue Connection Not Found");

			}
		} catch (Exception e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}

	}

	public byte[] consume(String address, QueueListener object, String queueName) throws Exception {

		ActiveMQConnectionFactory activeMQConnectionFactory = this.activeMQConnectionFactory;
		if (activeMQConnectionFactory == null) {
			throw new Exception("Invalid Connection Exception");
		}

		if (destination == null) {
			setup();
		}

		MessageConsumer consumer;
		try {
			destination = session.createQueue(address);
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(getListener(queueName, object));

		} catch (JMSException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ", "ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return null;
	}

	public static MessageListener getListener(String queueName, QueueListener object) {
		if (queueName.equals("ACTIVEMQ")) {

			return new MessageListener() {
				@Override
				public void onMessage(Message message) {
					object.setListener(message);
				}
			};

		}
		return null;
	}

	public Boolean send(byte[] message, String address) {
		boolean flag = false;

		try {
			initialSetup();
			destination = session.createQueue(address);
			MessageProducer messageProducer = session.createProducer(destination);
			BytesMessage byteMessage = session.createBytesMessage();
			byteMessage.writeObject(message);
			messageProducer.send(byteMessage);
			flag = true;
		} catch (JMSException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ", "ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return flag;
	}

	public Boolean send(String message, String address) {
		boolean flag = false;

		try {
			initialSetup();
			destination = session.createQueue(address);
			MessageProducer messageProducer = session.createProducer(destination);
			messageProducer.send(session.createTextMessage(message));
			flag = true;
		} catch (JMSException e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		} catch (Exception e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return flag;
	}

	private void initialSetup() throws Exception {
		if (this.activeMQConnectionFactory == null) {
			throw new Exception("Invalid Connection Exception");
		}
		setup();
	}

	public void connectActiveMQ() {
		try {
			List<PrintMQDetails> printQueueDetails = getQueueDetails();
			if (printQueueDetails != null && printQueueDetails.size() > 0) {

				for (int i = 0; i < printQueueDetails.size(); i++) {
					String outBoundAddress = printQueueDetails.get(i).getOutboundQueueName();
					outBoundQueue = outBoundAddress;
				}
			} else {
				throw new Exception("Queue Connection Not Found");
			}
		} catch (Exception e) {
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR : " + e.getMessage());
			logger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "ACTIVEMQ","ERROR MESSAGE : " + ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
	}
}
