package io.mosip.partner.partnermanagement.logger;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.partner.partnermanagement.util.LoggerFactory;


/**
 * The Class RegProcessorLogger.
 * @author : Rishabh Keshari
 */
public final class PartnerManagementLogger {

	private PartnerManagementLogger() {
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
