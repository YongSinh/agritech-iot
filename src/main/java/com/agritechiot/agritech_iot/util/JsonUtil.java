package com.agritechiot.agritech_iot.util;

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
}
