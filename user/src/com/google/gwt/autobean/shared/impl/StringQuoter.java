/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.autobean.shared.impl;

import com.google.gwt.autobean.server.impl.JsonSplittable;
import com.google.gwt.autobean.shared.Splittable;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class has a super-source version with a client-only implementation.
 *
 * <p><span style='color:red'>AutoBeans has moved to
 * <code>com.google.web.bindery.autobeans</code>.  This package will be
 * removed in a future version of GWT.</span></p>
 */
@Deprecated
public class StringQuoter {
  private static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSz";
  private static final DateFormat ISO8601 = new SimpleDateFormat(
      ISO8601_PATTERN, Locale.getDefault());

  private static final String RFC2822_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
  private static final DateFormat RFC2822 = new SimpleDateFormat(
      RFC2822_PATTERN, Locale.getDefault());

  /**
   * Create a quoted JSON string.
   */
  public static String quote(String raw) {
    return JSONObject.quote(raw);
  }

  public static Splittable split(String payload) {
    return JsonSplittable.create(payload);
  }

  /**
   * Attempt to parse an ISO-8601 date format. May return {@code null} if the
   * input cannot be parsed.
   */
  public static Date tryParseDate(String date) {
    try {
      return new Date(Long.parseLong(date));
    } catch (NumberFormatException ignored) {
    }
    if (date.endsWith("Z")) {
      date = date.substring(0, date.length() - 1) + "+0000";
    }
    try {
      return ISO8601.parse(date);
    } catch (ParseException ignored) {
    }
    try {
      return RFC2822.parse(date);
    } catch (ParseException ignored) {
    }
    return null;
  }
}
