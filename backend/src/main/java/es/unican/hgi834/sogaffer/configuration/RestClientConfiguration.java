package es.unican.hgi834.sogaffer.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    @Qualifier("userClient")
    public RestClient userRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.sorare.com/api/v1/users")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @Qualifier("graphQLClient")
    public RestClient graphQLRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.sorare.com/graphql")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
