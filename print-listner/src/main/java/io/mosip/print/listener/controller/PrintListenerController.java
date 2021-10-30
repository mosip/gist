package io.mosip.print.listener.controller;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.constant.LoggerFileConstant;
import io.mosip.print.listener.logger.PrintListenerLogger;
import io.mosip.print.listener.model.EventModel;
import io.mosip.print.listener.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class PrintListenerController {

	/** The printservice. */
	@Autowired
	private ClientService clientService;

	Logger logger = PrintListenerLogger.getLogger(PrintListenerController.class);
	/**
	 * Gets the file.
	 *
	 * @param eventModel the event details
	 * @return the file
	 * @throws Exception the reg print app exception
	 */
	@PostMapping(path = "/decryptCredentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleSubscribeEvent(@RequestBody EventModel eventModel) throws Exception {
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
				"/decryptCredentials", "Method handleSubscribeEvent(EventModel) call started");
		clientService.generateCard(eventModel);
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
				"/decryptCredentials", "Method handleSubscribeEvent(EventModel) call Completed");
		return new ResponseEntity<>("successfully printed", HttpStatus.OK);
	}

	public void processDataShareUrl(EventModel eventModel) {
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
				"/processDataShareUrl", "Method processDataShareUrl(EventModel) call started");
		clientService.generateCard(eventModel);
		logger.info(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
				"/processDataShareUrl", "Method processDataShareUrl(EventModel) call Completed");
	}
}
