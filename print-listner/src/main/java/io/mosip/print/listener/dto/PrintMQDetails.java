package io.mosip.print.listener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrintMQDetails {
    private String name;
    private String brokerUrl;
    private String inboundQueueName;
    private String outboundQueueName;
    private String userName;
    private String password;
    private String typeOfQueue;
}
