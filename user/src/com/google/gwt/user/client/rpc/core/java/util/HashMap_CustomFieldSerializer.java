/*
 * Copyright 2008 Google Inc.
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
package com.google.gwt.user.client.rpc.core.java.util;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import java.util.HashMap;

/**
 * Custom field serializer for {@link java.util.HashMap}.
 */
public final class HashMap_CustomFieldSerializer extends
    CustomFieldSerializer<HashMap> {

  @SuppressWarnings("unchecked")
  public static void deserialize(SerializationStreamReader streamReader,
      HashMap instance) throws SerializationException {
    Map_CustomFieldSerializerBase.deserialize(streamReader, instance);
  }

  @SuppressWarnings("unchecked")
  public static void serialize(SerializationStreamWriter streamWriter,
      HashMap instance) throws SerializationException {
    Map_CustomFieldSerializerBase.serialize(streamWriter, instance);
  }

  public void deserializeInstance(SerializationStreamReader streamReader,
      HashMap instance) throws SerializationException {
    deserialize(streamReader, instance);
  }

  public void serializeInstance(SerializationStreamWriter streamWriter,
      HashMap instance) throws SerializationException {
    serialize(streamWriter, instance);
  }
}
