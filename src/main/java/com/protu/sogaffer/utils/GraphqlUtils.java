package com.protu.sogaffer.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

public class GraphqlUtils {

    public static String loadQuery(String filename) throws IOException {
        ClassPathResource resource = new ClassPathResource("graphql/" + filename + ".graphql");
        try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}