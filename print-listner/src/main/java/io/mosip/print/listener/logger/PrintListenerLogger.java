package io.mosip.print.listener.logger;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.constant.LogMessageTypeConstant;
import io.mosip.print.listener.util.ApplicationResourceContext;
import io.mosip.print.listener.util.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * The Class RegProcessorLogger.
 * @author : Rishabh Keshari
 */
public final class PrintListenerLogger {
	public static List<LogMessage>  logMessageList = new ArrayList<>();

	private PrintListenerLogger() {
	}

	/**
	 * Gets the logger.
	 *
	 * @param clazz the clazz
	 * @return the logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		return LoggerFactory.getLogger(clazz);
	}

	public static void println(LogMessageTypeConstant type, String message) {
		LogMessage logMessage = new LogMessage(type, (new Date()) + " : " +
				ApplicationResourceContext.getInstance().getLabelBundle().getString(type.toString()) + " : " + message);
		logMessageList.add(logMessage);
	}
}
