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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.SmartLifecycle;
import org.summerclouds.common.core.tool.MSystem;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

public class SpanLogsAppender extends UnsynchronizedAppenderBase<ILoggingEvent>
        implements SmartLifecycle {

    @Autowired private Tracer tracer;

    /**
     * This is called only for configured levels. It will not be executed for DEBUG level if root
     * logger is INFO.
     */
    @Override
    protected void append(ILoggingEvent event) {
        final Span currentSpan = tracer.currentSpan();
        if (currentSpan == null) return;

        String trace = "";
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy instanceof ThrowableProxy) {
            Throwable throwable = ((ThrowableProxy) throwableProxy).getThrowable();
            if (throwable != null) {

                if (event.getLevel() == Level.ERROR) currentSpan.error(throwable);
                trace = MSystem.currentStackTrace(" ");
            }
        }
        if (event.getLevel() == Level.ERROR) currentSpan.tag("error", "true");
        currentSpan.event(
                event.getLevel() + " " + event.getLoggerName() + " " + event.getMessage() + trace);
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}
