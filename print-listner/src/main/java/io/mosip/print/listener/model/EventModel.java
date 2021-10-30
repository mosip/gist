package io.mosip.print.listener.model;

import lombok.Data;

@Data
public class EventModel {
	private String requestedOn;
	private Event event;
}
