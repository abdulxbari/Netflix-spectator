/*
 * Copyright 2014-2022 Netflix, Inc.
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
package com.netflix.spectator.impl.matcher;

import com.netflix.spectator.impl.PatternMatcher;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.regex.Pattern;

public class ExpandOrPatternMatcherTest extends AbstractPatternMatcherTest {

  @Override
  protected void testRE(String regex, String value) {
    Pattern pattern = Pattern.compile("^.*(" + regex + ")", Pattern.DOTALL);
    List<PatternMatcher> matchers = PatternMatcher.compile(regex).expandOrClauses(1000);
    if (pattern.matcher(value).find()) {
      Assertions.assertTrue(matches(matchers, value), regex + " should match " + value);
    } else {
      Assertions.assertFalse(matches(matchers, value), regex + " shouldn't match " + value);
    }
  }

  private boolean matches(List<PatternMatcher> matchers, String value) {
    for (PatternMatcher m : matchers) {
      if (m.matches(value))
        return true;
    }
    return false;
  }
}
