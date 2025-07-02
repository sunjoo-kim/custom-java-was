package com.company.was.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public class ConfigLoader {
    public static Config load(String path) {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(path);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(inputStream, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}