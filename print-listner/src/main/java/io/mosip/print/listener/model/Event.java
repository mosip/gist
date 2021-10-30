package io.mosip.print.listener.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Event {
    private String id; //uuid
    private String dataShareUri; //URL
    private String printId; //Print Request Id
}