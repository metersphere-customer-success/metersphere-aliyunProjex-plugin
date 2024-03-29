package io.metersphere.platform.domain.oss;/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


public class ProgressEvent {
    private final long bytes;
    private final ProgressEventType eventType;

    public ProgressEvent(ProgressEventType eventType) {
        this(eventType, 0);
    }

    public ProgressEvent(ProgressEventType eventType, long bytes) {
        if (eventType == null) {
            throw new IllegalArgumentException("eventType must not be null.");
        }
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes transferred must be non-negative");
        }
        this.eventType = eventType;
        this.bytes = bytes;
    }

    public long getBytes() {
        return bytes;
    }

    public ProgressEventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return eventType + ", bytes: " + bytes;
    }
}
