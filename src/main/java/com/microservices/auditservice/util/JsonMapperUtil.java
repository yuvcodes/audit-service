package com.microservices.auditservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.auditservice.entity.AuditLog;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class JsonMapperUtil {

    private final ObjectMapper objectMapper;

    public JsonMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public <T> T fromJson(String jsonString, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public void writeValue(String fileName, List<AuditLog> oldLogs) throws IOException {
        objectMapper.writeValue(new File(fileName), oldLogs);
    }
}

