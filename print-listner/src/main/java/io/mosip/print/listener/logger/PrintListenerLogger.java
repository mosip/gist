package io.mosip.print.listener.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class RegProcessorLogger.
 * @author : Rishabh Keshari
 */
public final class PrintListenerLogger {

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
}
