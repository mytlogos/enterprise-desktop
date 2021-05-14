package com.mytlogos.enterprisedesktop.background.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mytlogos.enterprisedesktop.Formatter;

import java.time.LocalDateTime;

import java.lang.reflect.Type;

class GsonAdapter {
    private GsonAdapter() {

    }

    public static class LocalDateTimeAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return json == null ? null : Formatter.parseLocalDateTime(json.getAsString());
        }

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(Formatter.isoFormat(src));
        }
    }

    public static class ArrayAdapter implements JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return json == null ? null : Formatter.parseLocalDateTime(json.getAsString());
        }

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(Formatter.isoFormat(src));
        }
    }
}
