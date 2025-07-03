package com.agritechiot.logs.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
        // Private constructor to prevent instantiation
    }

    public static String objectToJsonString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    public static JsonNode parseJson(String jsonString) throws Exception {
        return objectMapper.readTree(jsonString);
    }

    /**
     * Converts a Java object to a JSON string.
     *
     * @param object the Java object to serialize
     * @return the JSON string
     * @throws Exception if the object serialization fails
     */
    public static String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Convert JSON string to given class type.
     *
     * @param json  the input JSON string
     * @param clazz the target class type
     * @return instance of the class with JSON data
     * @throws RuntimeException if conversion fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException("❌ Failed to deserialize JSON to " + clazz.getSimpleName());
        }
    }
}
