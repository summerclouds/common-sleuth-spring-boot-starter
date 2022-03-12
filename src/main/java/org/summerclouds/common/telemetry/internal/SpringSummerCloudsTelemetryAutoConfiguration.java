package org.summerclouds.common.telemetry.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.summerclouds.common.core.tracing.ITracing;
import org.summerclouds.common.telemetry.SpanLogsAppender;
import org.summerclouds.common.telemetry.TelemetryTracing;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

@Configuration
@ConfigurationProperties(prefix = "org.summerclouds.common.core")
public class SpringSummerCloudsTelemetryAutoConfiguration {

	@Bean
	ITracing telemetryTracing() {
		return new TelemetryTracing();
	}
	
	@Bean
	Appender<ILoggingEvent> spanLogsAppender() {
		return new SpanLogsAppender();
	}
	
	@Bean
	Tracer openTelemtryTracer() {
		 return GlobalOpenTelemetry.getTracer("opentel-example", "1.0");
	}
	
}
