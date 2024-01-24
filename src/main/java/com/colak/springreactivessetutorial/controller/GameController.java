package com.colak.springreactivessetutorial.controller;

import com.colak.springreactivessetutorial.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final Sinks.Many<Game> gameSink;

    private final Flux<Game> gameFlux;


    @PostMapping("/update")
    public Mono<Void> updateGame(@RequestBody Game game) {
        gameSink.tryEmitNext(game);
        return Mono.empty();
    }

    // http:localhost:8080/api/v1/games/stream

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> getGameUpdates() {
        return this.gameFlux;
    }
}
