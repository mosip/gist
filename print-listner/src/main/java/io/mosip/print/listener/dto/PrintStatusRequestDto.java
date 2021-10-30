package io.mosip.print.listener.dto;

import io.mosip.print.listener.constant.PrintTransactionStatus;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class PrintStatusRequestDto {
    private String id;
    private PrintTransactionStatus printStatus;
    private String statusComments;
    private String processedTime;
}
