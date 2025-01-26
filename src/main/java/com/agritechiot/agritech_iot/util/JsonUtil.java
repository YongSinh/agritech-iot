package com.agritechiot.agritech_iot.util;

import com.fasterxml.jackson.databind.ObjectMapper;
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a JSON string to a Java object of the specified type.
     *
     * @param json  the JSON string
     * @param clazz the target class to map the JSON to
     * @param <T>   the type of the target class
     * @return the mapped object
     * @throws Exception if the JSON parsing fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
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
