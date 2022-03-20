package org.summerclouds.common.sleuth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.summerclouds.common.core.tracing.Getter;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.tracing.ITracing;
import org.summerclouds.common.core.tracing.Setter;

import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.Propagation;
import brave.propagation.TraceContextOrSamplingFlags;

public class SleuthTracing implements ITracing {

	@Autowired
	private Tracer tracer;
	@Autowired 
	private Tracing tracing;
	
	@Override
	public ISpan current() {
		Span span = tracer.currentSpan();
		return new SpanImpl(span);
	}

	@Override
	public IScope enter(ISpan parent, String name, Object ... keyValue) {
		Span parentSpan = ((SpanImpl)parent).span();
		Span newSpan = null;
		try (Tracer.SpanInScope ws = tracer.withSpan(parentSpan.start())) {
			newSpan = tracer.nextSpan().name(name);
		}
		newSpan.tag("_thread", Thread.currentThread().toString());
		for (int i = 0; i < keyValue.length-1; i=i+2)
			newSpan.tag(String.valueOf(keyValue[i]), String.valueOf(keyValue[i+1]));

		return new ScopeImpl(tracer, newSpan);
	}

	@Override
	public void cleanup() {
		tracer.withSpan(null);
	}

	@Override
	public IScope enter(String name, Object ... keyValue) {
		Span newSpan = tracer.nextSpan().name(name);
		for (int i = 0; i < keyValue.length-1; i=i+2)
			newSpan.tag(String.valueOf(keyValue[i]), String.valueOf(keyValue[i+1]));
		newSpan.tag("_thread", Thread.currentThread().toString());
		return new ScopeImpl(tracer, newSpan);

	}

	@Override
	public String getTraceId() {
		Span span = tracer.currentSpan();
		if (span == null) return "";
		return span.context().traceId();
	}

	@Override
	public String getSpanId() {
		Span span = tracer.currentSpan();
		if (span == null) return "";
		return span.context().spanId();
	}

	@Override
	public void inject(Setter<String> setter) {
		// XXX not sure if it's working !!!
		CurrentTraceContext context = tracing.currentTraceContext();
		
		tracing.propagation().injector(new Propagation.Setter<Setter<String>, String>() {

			@Override
			public void put(Setter<String> request, String key, String value) {
				request.set(key, value);
			}
		}).inject( context.get() , setter);
	}

	@Override
	public IScope extract(Getter<String> getter) {
		// XXX not sure if it's working !!!
		@SuppressWarnings("unused")
		TraceContextOrSamplingFlags out = tracing.propagation().extractor(new Propagation.Getter<Getter<String>, String>() {

			@Override
			public String get(Getter<String> request, String key) {
				return request.get(key);
			}
		}).extract(getter);

		Span span = tracer.currentSpan();
		return new ScopeImpl(tracer, span);
	}

}
