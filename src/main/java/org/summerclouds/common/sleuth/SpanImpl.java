package org.summerclouds.common.sleuth;

import org.springframework.cloud.sleuth.Span;
import org.summerclouds.common.core.tracing.ISpan;

public class SpanImpl implements ISpan {

	private Span span;

	public SpanImpl(Span span) {
		this.span = span;
	}

	public Span span() {
		return span;
	}

	public void set(String key, String value) {
		span.tag(key, String.valueOf(value));
	}

	public void set(String key, boolean value) {
		span.tag(key, String.valueOf(value));
	}

	public void set(String key, double value) {
		span.tag(key, String.valueOf(value));
	}

	public void set(String key, long value) {
		span.tag(key, String.valueOf(value));
	}

	@Override
	public void exception(Throwable exception) {
		span.tag("exception", String.valueOf(exception)); //XXX
	}

	@Override
	public void setError(String error) {

	}

	@Override
	public void setTag(String key, String value) {
		span.tag(key, value);
	}

	@Override
	public void log(String message) {
    	span.event(message);
	}

}
