/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        } catch (Exception t) {
        }
        try {
            span.span().end();
        } catch (Exception t) {
        }
    }

    @Override
    public ISpan getSpan() {
        return span;
    }
}
