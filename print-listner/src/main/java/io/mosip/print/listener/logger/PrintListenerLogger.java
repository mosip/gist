package io.mosip.print.listener.logger;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.print.listener.util.LoggerFactory;


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
