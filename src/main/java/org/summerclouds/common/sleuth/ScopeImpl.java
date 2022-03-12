package org.summerclouds.common.sleuth;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;

public class ScopeImpl implements IScope {

	private SpanImpl span;
	private Tracer.SpanInScope ws;

	public ScopeImpl(Tracer tracer, Span newSpan) {
		span = new SpanImpl(newSpan);
		ws = tracer.withSpan(newSpan.start());
	}

	@Override
	public void close() {
		try {
			ws.close();
		} catch (Throwable t) {}
		try {
			span.span().end();
		} catch (Throwable t) {}
	}

	@Override
	public ISpan getSpan() {
		return span;
	}

}
