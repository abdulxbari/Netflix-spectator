/*
 * Copyright 2014-2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spectator.api.histogram;

import com.netflix.spectator.api.Counter;
import com.netflix.spectator.api.DefaultRegistry;
import com.netflix.spectator.api.Functions;
import com.netflix.spectator.api.Registry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class BucketCounterTest {

  private long sum(Registry r, String name) {
    return r.counters().filter(Functions.nameEquals(name)).mapToLong(Counter::count).sum();
  }

  @Test
  public void basic() {
    Registry r = new DefaultRegistry();
    BucketCounter c = BucketCounter.get(
        r, r.createId("test"), BucketFunctions.latency(4, TimeUnit.SECONDS));

    c.record(TimeUnit.MILLISECONDS.toNanos(3750));
    Assertions.assertEquals(1, r.counters().count());
    Assertions.assertEquals(1, sum(r, "test"));

    c.record(TimeUnit.MILLISECONDS.toNanos(4221));
    Assertions.assertEquals(2, r.counters().count());
    Assertions.assertEquals(2, sum(r, "test"));

    c.record(TimeUnit.MILLISECONDS.toNanos(4221));
    Assertions.assertEquals(2, r.counters().count());
    Assertions.assertEquals(3, sum(r, "test"));
  }


  @Test
  public void increment() {
    Registry r = new DefaultRegistry();
    BucketCounter c = BucketCounter.get(
        r, r.createId("test"), BucketFunctions.latency(4, TimeUnit.SECONDS));

    c.record(TimeUnit.MILLISECONDS.toNanos(3750));
    Assertions.assertEquals(1, r.counters().count());
    Assertions.assertEquals(1, sum(r, "test"));

    c.increment(TimeUnit.MILLISECONDS.toNanos(3750), 5);
    Assertions.assertEquals(1, r.counters().count());
    Assertions.assertEquals(6, sum(r, "test"));
  }

}
