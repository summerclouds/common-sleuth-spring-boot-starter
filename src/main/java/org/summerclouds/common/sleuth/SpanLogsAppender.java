package org.summerclouds.common.sleuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.SmartLifecycle;
import org.summerclouds.common.core.tool.MSystem;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class SpanLogsAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements SmartLifecycle  {

	@Autowired
	private Tracer tracer;

    /**
     * This is called only for configured levels.
     * It will not be executed for DEBUG level if root logger is INFO.
     */
    @Override
    protected void append(ILoggingEvent event) {
    	final Span currentSpan = tracer.currentSpan();
    	if (currentSpan == null) return;
    	
    	String trace = "";
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy instanceof ThrowableProxy) {
            Throwable throwable = ((ThrowableProxy)throwableProxy).getThrowable();
            if (throwable != null) {
            	
            	if (event.getLevel() == Level.ERROR)
            		currentSpan.error(throwable);
            	trace = MSystem.currentStackTrace(" ");
            }
        }
    	if (event.getLevel() == Level.ERROR)
    		currentSpan.tag("error", "true");
    	currentSpan.event(event.getLevel() + " " + event.getLoggerName() + " " + event.getMessage() + trace );
        
    }

	@Override
	public boolean isRunning() {
		return isStarted();
	}
}