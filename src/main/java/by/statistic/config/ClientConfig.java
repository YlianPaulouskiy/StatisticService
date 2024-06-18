package by.statistic.config;

import by.statistic.api.client.PmClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    @Value("${pm.service.url}")
    private String pmServiceUrl;

    @Bean
    public WebClient pmWebClient() {
        return WebClient.builder()
                .baseUrl(pmServiceUrl)
                .build();
    }

    @Bean
    public PmClient pmClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory.builder()
                        .exchangeAdapter(WebClientAdapter.create(pmWebClient()))
                        .build();
        return httpServiceProxyFactory.createClient(PmClient.class);
    }

}
