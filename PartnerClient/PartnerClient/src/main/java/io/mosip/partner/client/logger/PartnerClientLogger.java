package io.mosip.partner.client.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class RegProcessorLogger.
 * @author : Rishabh Keshari
 */
public final class PartnerClientLogger {

	private PartnerClientLogger() {
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
