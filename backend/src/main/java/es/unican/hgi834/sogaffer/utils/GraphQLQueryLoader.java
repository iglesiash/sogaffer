package es.unican.hgi834.sogaffer.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GraphQLQueryLoader {

    /**
     * Retrieves the GraphQL mutation for the "SignIn" operation as a string.
     *
     * @return the content of the "SignInMutation" GraphQL file.
     * @throws IllegalStateException if the file could not be found or read.
     */
    public static String getSignInMutation() {
        return load("auth/signInMutation");
    }

    /**
     * Loads the content of a GraphQL query file from the classpath.
     *
     * @param fileName the name of the GraphQL file (without extension) located in the "graphql/" directory
     * @return the content of the specified GraphQL query file as a string
     * @throws IllegalStateException if the file could not be found or read
     */
    private static String load(String fileName) throws IllegalStateException {
        try {
            ClassPathResource resource = new ClassPathResource("graphql/" + fileName + ".graphql");
            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("GraphQL query file not found: " + fileName, e);
        }
    }
}