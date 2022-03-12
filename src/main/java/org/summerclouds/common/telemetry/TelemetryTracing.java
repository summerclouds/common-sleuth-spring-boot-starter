package org.summerclouds.common.telemetry;

import javax.annotation.PostConstruct;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.tracing.ITracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;

public class TelemetryTracing implements ITracing {

	@PostConstruct
	public void setup() {
	}
	
	@Override
	public ISpan current() {
		Span span = Span.current();
		return new SpanImpl(span);
	}

	@Override
	public IScope enter(ISpan span, String name, Object ... keyValue) {
		Span current = ((SpanImpl)span).span();
		return enter(current, name, keyValue);
	}

	private void setAttribute(Span next, Object key, Object value) {
		if (value == null) return;
		String k = String.valueOf(key);
		if (value instanceof Long)
			next.setAttribute(k, (Long)value);
		else
		if (value instanceof Boolean)
			next.setAttribute(k, (Boolean)value);
		else
		if (value instanceof Double)
			next.setAttribute(k, (Double)value);
		else
			next.setAttribute(k, String.valueOf(value));
	}

	@Override
	public void cleanup() {
		//XXX
	}

	@Override
	public IScope enter(String name, Object ... keyValue) {
		Span current = Span.current();
		return enter(current, name, keyValue);
	}

	protected IScope enter(Span current, String name, Object[] keyValue) {
		Tracer tracer = M.l(Tracer.class);
		
		Span next = tracer.spanBuilder(name)
                .setParent(Context.current().with(current))
                .startSpan();
		
		for (int i = 0; i < keyValue.length-1; i=i+2)
			setAttribute(next, keyValue[i], keyValue[i+1]);
		next.makeCurrent();
		return new ScopeImpl(current,next);
	}

	@Override
	public String getCurrentId() {
		return ""; // XXX
	}

}
