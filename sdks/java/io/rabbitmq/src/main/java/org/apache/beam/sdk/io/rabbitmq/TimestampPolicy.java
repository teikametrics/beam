/*
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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.sdk.io.rabbitmq;

import java.io.Serializable;
import org.joda.time.Instant;

/**
 * A timestamp policy to assign event time for messages from a RabbitMq queue and watermark for it.
 *
 * <p>Inspired by a similar concept if KafkaIO
 */
public abstract class TimestampPolicy {

  /**
   * Analgous to KafkaIO's TimestampPolicy.PartitionContext but the backlog is a backwards-looking
   * boolean rather than forwards-looking, depth-aware metric.
   */
  public abstract static class LastRead implements Serializable {
    /**
     * @return {@code true} if the last read from rabbit resulted in a message being delivered,
     *     {@code false} if the last read was from an empty queue
     */
    public abstract boolean hasBacklog();

    /**
     * @return the timestamp (wall clock) at which the last read was attempted, regardless of
     *     whether a message was available
     */
    public abstract Instant lastCheckedAt();
  }

  public abstract Instant getTimestampForRecord(RabbitMqMessage record);

  public abstract Instant getWatermark(LastRead context, RabbitMqMessage record);
}