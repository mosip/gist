package io.mosip.print.listener.controller;

import io.mosip.print.listener.model.EventModel;
import io.mosip.print.listener.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	/**
	 * Gets the file.
	 *
	 * @param printRequest the print request DTO
	 * @param token        the token
	 * @param errors       the errors
	 * @param printRequest the print request DTO
	 * @return the file
	 * @throws Exception
	 * @throws RegPrintAppException the reg print app exception
	 */
	@PostMapping(path = "/decryptCredentials", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> handleSubscribeEvent(@RequestBody EventModel eventModel) throws Exception {
		clientService.generateCard(eventModel);
		return new ResponseEntity<>("successfully printed", HttpStatus.OK);
	}

	public void processDataShareUrl(EventModel eventModel) {
		clientService.generateCard(eventModel);
	}
}
