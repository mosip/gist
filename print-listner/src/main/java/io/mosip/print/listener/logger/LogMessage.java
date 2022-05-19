package io.mosip.print.listener.logger;

import io.mosip.print.listener.constant.LogMessageTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LogMessage {
    private LogMessageTypeConstant messageType;
    private String message;
}
