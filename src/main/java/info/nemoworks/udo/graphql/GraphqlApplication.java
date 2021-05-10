package info.nemoworks.udo.graphql;

import com.google.common.eventbus.EventBus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "info.nemoworks.udo")
public class GraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphqlApplication.class, args);
    }

    @Bean
    public EventBus eventBus(){
        return new EventBus();
    }
}
