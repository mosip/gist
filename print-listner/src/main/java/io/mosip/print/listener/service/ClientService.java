package io.mosip.print.listener.service;

import io.mosip.print.listener.model.EventModel;

public interface ClientService {
    public void generateCard(EventModel eventModel);
}
