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
        span.tag("exception", String.valueOf(exception)); // XXX
    }

    @Override
    public void setError(String error) {}

    @Override
    public void setTag(String key, String value) {
        span.tag(key, value);
    }

    @Override
    public void log(String message) {
        span.event(message);
    }
}
