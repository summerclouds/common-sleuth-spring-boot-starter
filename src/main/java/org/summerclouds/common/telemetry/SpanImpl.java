package org.summerclouds.common.telemetry;

import org.summerclouds.common.core.tracing.ISpan;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;

public class SpanImpl implements ISpan {

	private Span span;

	public SpanImpl(Span span) {
		this.span = span;
	}

	public Span span() {
		return span;
	}

	public void set(String key, String value) {
		span.setAttribute(key, value);
	}

	public void set(String key, boolean value) {
		span.setAttribute(key, value);
	}

	public void set(String key, double value) {
		span.setAttribute(key, value);
	}

	public void set(String key, long value) {
		span.setAttribute(key, value);
	}

	@Override
	public void record(Throwable exception) {
		span.recordException(exception);
	}

	@Override
	public void setError(String error) {
		span.setStatus(StatusCode.ERROR, error);
	}

}
