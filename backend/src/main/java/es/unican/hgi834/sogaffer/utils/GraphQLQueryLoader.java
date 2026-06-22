package es.unican.hgi834.sogaffer.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GraphQLQueryLoader {

    public static String getSignInMutation() {
        return load("auth/signInMutation");
    }

    private static String load(String fileName) throws IllegalStateException {
        try {
            ClassPathResource resource = new ClassPathResource("graphql/" + fileName + ".graphql");
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("GraphQL query file not found: " + fileName, e);
        }
    }
}