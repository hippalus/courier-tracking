package com.courier.eventprocessor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Utils {
    private Utils() {
        //DO NOTING
    }

    private static ObjectMapper mapper;

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.registerModule(new JavaTimeModule());
        }
        return mapper;
    }

    public static String getJsonString(Object o) throws JsonProcessingException {
        return getMapper().writeValueAsString(o);

    }

    public static <T> T jsonToPojo(String content, Class<T> clazz) throws JsonProcessingException {
        return getMapper().readValue(content, clazz);
    }


}
