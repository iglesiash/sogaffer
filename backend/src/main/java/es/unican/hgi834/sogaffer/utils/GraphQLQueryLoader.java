package es.unican.hgi834.sogaffer.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GraphQLQueryLoader {

    private GraphQLQueryLoader() {
        throw new IllegalStateException("Utility class");
    }

    public static String getSignInMutation() {
        return load("auth/signInMutation");
    }

    private static String load(String fileName) {
        try {
            ClassPathResource resource = new ClassPathResource("graphql/" + fileName + ".graphql");
            String fileContent = resource.getContentAsString(StandardCharsets.UTF_8);
            return replaceAud(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("GraphQL query file not found: " + fileName, e);
        }
    }

    private static String replaceAud(String input) {
        return input.replace("%s", "SoGaffer");
    }
}