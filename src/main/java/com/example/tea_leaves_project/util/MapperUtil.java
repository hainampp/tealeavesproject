package com.example.tea_leaves_project.util;

import com.example.tea_leaves_project.exception.ApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperUtil {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Parse json error", e);
            throw ApiException.ErrDataLoss().build();
        }
    }

    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Write to json error", e);
            throw ApiException.ErrDataLoss().build();
        }
    }
}
