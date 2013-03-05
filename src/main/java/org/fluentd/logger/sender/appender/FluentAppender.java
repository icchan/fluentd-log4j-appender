package org.fluentd.logger.sender.appender;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.fluentd.logger.FluentLogger;

/**
 * Basic implementation of a fluent appender for log4j
 */
public class FluentAppender extends AppenderSkeleton {

	private FluentLogger fluentLogger;
	
	protected String tag = "log4j-appender";
	
	protected String host = "localhost";

	protected int port = 24224;
	
	protected String label = "label";

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Initialise the fluent logger
	 */
	@Override
	public void activateOptions() {
		try {
			fluentLogger = FluentLogger.getLogger(tag, host, port);
		} catch (RuntimeException e) {
			e.printStackTrace(); // TODO replace this with something useful
		}
		super.activateOptions();
	}
	
	/**
	 * Flush the fluent logger before closing
	 */
	@Override
	public void close() {
		fluentLogger.flush();
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	/**
	 * Write to the log
	 */
	@Override
	protected void append(LoggingEvent event) {
		Map<String, Object> messages = new HashMap<String, Object>();
		messages.put("level", event.getLevel().toString());
		messages.put("loggerName", event.getLoggerName());
		messages.put("thread", event.getThreadName());
		messages.put("message", event.getMessage().toString());
		
		fluentLogger.log(label, messages, event.getTimeStamp());
	}

}
