package io.mosip.print.listener.activemq;

import javax.jms.Message;

public abstract class QueueListener {
	
	public abstract void setListener(Message message);

}
