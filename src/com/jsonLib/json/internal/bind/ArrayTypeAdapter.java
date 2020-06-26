/*
 * Copyright (C) 2011 JsonLib Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsonLib.json.internal.bind;

import com.jsonLib.json.TypeAdapterFactory;
import com.jsonLib.json.internal.$Gson$Types;
import com.jsonLib.json.stream.JsonReader;
import com.jsonLib.json.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.jsonLib.json.JsonLib;
import com.jsonLib.json.TypeAdapter;
import com.jsonLib.json.reflect.TypeToken;
import com.jsonLib.json.stream.JsonToken;

/**
 * Adapt an array of objects.
 */
public final class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override public <T> TypeAdapter<T> create(JsonLib gson, TypeToken<T> typeToken) {
      Type type = typeToken.getType();
      if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
        return null;
      }

      Type componentType = $Gson$Types.getArrayComponentType(type);
      TypeAdapter<?> componentTypeAdapter = gson.getAdapter(TypeToken.get(componentType));
      return new ArrayTypeAdapter(
              gson, componentTypeAdapter, $Gson$Types.getRawType(componentType));
    }
  };

  private final Class<E> componentType;
  private final TypeAdapter<E> componentTypeAdapter;

  public ArrayTypeAdapter(JsonLib context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
    this.componentTypeAdapter =
      new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
    this.componentType = componentType;
  }

  @Override public Object read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }

    List<E> list = new ArrayList<E>();
    in.beginArray();
    while (in.hasNext()) {
      E instance = componentTypeAdapter.read(in);
      list.add(instance);
    }
    in.endArray();

    int size = list.size();
    Object array = Array.newInstance(componentType, size);
    for (int i = 0; i < size; i++) {
      Array.set(array, i, list.get(i));
    }
    return array;
  }

  @SuppressWarnings("unchecked")
  @Override public void write(JsonWriter out, Object array) throws IOException {
    if (array == null) {
      out.nullValue();
      return;
    }

    out.beginArray();
    for (int i = 0, length = Array.getLength(array); i < length; i++) {
      E value = (E) Array.get(array, i);
      componentTypeAdapter.write(out, value);
    }
    out.endArray();
  }
}
