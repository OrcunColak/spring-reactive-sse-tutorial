package com.colak.springreactivessetutorial.config;

import com.colak.springreactivessetutorial.model.Game;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SinkConfig {
    @Bean
    public Sinks.Many<Game> sink() {
        // Sinks.Many is a specialized Sink that can have multiple subscribers.
        return Sinks.many()
                // The replay().limit(1) part means that it will replay the last emitted element (due to limit(1))
                // to any late subscribers.
                .replay()
                .limit(1);
    }

    @Bean
    public Flux<Game> gameBroadcast(Sinks.Many<Game> sink) {
        // It takes the Sinks.Many<Game> object produced by sink() as an argument and converts it into a Flux using
        // the asFlux() method.
        // A Flux is a publisher that can emit 0 to N elements.
        return sink.asFlux();
    }
}
