package com.ebao.teamsapproval.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
@ConfigurationProperties(prefix = "webhook")
public class ApprovalWorkflowConfig {
    private String url;
    private static final Logger log = LoggerFactory.getLogger(ApprovalWorkflowConfig.class);

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info("Headers configuration sent by WebClient: {}", clientRequest.headers());
                    return Mono.just(clientRequest);
                }));
    }
}
