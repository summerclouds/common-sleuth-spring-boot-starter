package org.summerclouds.common.sleuth.internal;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.summerclouds.common.core.tracing.ITracing;
import org.summerclouds.common.sleuth.SleuthTracing;
import org.summerclouds.common.sleuth.SpanLogsAppender;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

@Configuration
public class SpringSummerCloudsSleuthAutoConfiguration {

	@Bean
	ITracing telemetryTracing() {
		return new SleuthTracing();
	}
	
	@Bean
	@ConditionalOnProperty(name = "org.summerclouds.common.sleuth.tracelog.enabled",havingValue = "true")
	Appender<ILoggingEvent> spanLogsAppender() {
		return new SpanLogsAppender();
	}
		
}
