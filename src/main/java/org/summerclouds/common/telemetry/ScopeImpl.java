package org.summerclouds.common.telemetry;

import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;

import io.opentelemetry.api.trace.Span;

public class ScopeImpl implements IScope {

	private SpanImpl span;
	private Span parent;

	public ScopeImpl(Span parent, Span current) {
		this.parent = parent;
		span = new SpanImpl(current);
	}

	@Override
	public void close() {
		span.span().end();
//		if (parent != null)
//			parent.makeCurrent();
	}

	@Override
	public ISpan getSpan() {
		return span;
	}

}
