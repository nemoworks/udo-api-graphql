package info.nemoworks.udo.graphql.config;

import info.nemoworks.udo.messaging.HTTPServiceGateway;
import info.nemoworks.udo.messaging.MessagingManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UdoGatewayConfig {
    @Bean
    public HTTPServiceGateway httpServiceGateway(){
        HTTPServiceGateway gateway = new HTTPServiceGateway(this.messagingManager());
        return gateway;
    }

    @Bean
    public MessagingManager messagingManager(){
        return new MessagingManager();
    }
}
