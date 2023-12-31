/*
 * Copyright 2014-2020 Netflix, Inc.
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
package com.netflix.spectator.spark;

import com.netflix.spectator.sidecar.SidecarConfig;
import com.typesafe.config.Config;

import java.util.HashMap;
import java.util.Map;

class SpectatorConfig implements SidecarConfig {

  private final Config config;

  SpectatorConfig(Config config) {
    this.config = config;
  }

  @Override
  public String get(String k) {
    return config.hasPath(k) ? config.getString(k) : null;
  }

  @Override
  public String outputLocation() {
    return config.getString("output-location");
  }

  @Override
  public Map<String, String> commonTags() {
    Map<String, String> tags = new HashMap<>(SidecarConfig.super.commonTags());
    for (Config cfg : config.getConfigList("tags")) {
      // These are often populated by environment variables that can sometimes be empty
      // rather than not set when missing. Empty strings are not allowed by Atlas.
      String value = cfg.getString("value");
      if (!value.isEmpty()) {
        tags.put(cfg.getString("key"), cfg.getString("value"));
      }
    }
    return tags;
  }
}
