package io.mosip.partner.partnermanagement.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.partner.partnermanagement.util.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This utils contains exception utilities.
 * 
 * @author Shashank Agrawal
 * @author Ritesh Sinha
 * @since 1.0.0
 *
 */
public final class ExceptionUtils {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

	private ExceptionUtils() {

	}

	/**
	 * Returns an String object that can be used after building the exception stack
	 * trace.
	 * 
	 * @param message the exception message
	 * @param cause   the cause
	 * @return the exception stack
	 */
	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("\n");
			sb.append("nested exception is ").append(cause);
			return sb.toString();
		} else {
			return message;
		}
	}

	/**
	 * This method returns the stack trace
	 * 
	 * @param throwable the exception to be added to the list of exception
	 * @return the stack trace
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}


	/**
	 * This method provide jsonvalue based on propname mention.
	 * 
	 * @param node     the jsonnode.
	 * @param propName the property name.
	 * @return the property value.
	 */
	private static String getJsonValue(JsonNode node, String propName) {
		if (node.get(propName) != null) {
			return node.get(propName).asText();
		}
		return null;
	}

	public static void logRootCause(Throwable exception) {
		logger.error("Exception Root Cause: {} ", exception.getMessage());
		logger.debug("Exception Root Cause:", exception);
	}
}
