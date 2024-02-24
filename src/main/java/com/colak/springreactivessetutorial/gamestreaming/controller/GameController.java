package com.colak.springreactivessetutorial.gamestreaming.controller;

import com.colak.springreactivessetutorial.gamestreaming.model.Game;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
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

    private final Faker faker = new Faker();


    @PostMapping("/update")
    public Mono<Void> updateGame(@RequestBody Game game) {
        emitNext(game);
        return Mono.empty();
    }

    // Update every 5 seconds
    @Scheduled(fixedRate = 5_000)
    public void updateGameRandomly() {
        Game fakeGame = new Game();
        fakeGame.setScoreTeamA(faker.number().numberBetween(0, 10));
        fakeGame.setScoreTeamB(faker.number().numberBetween(0, 10));
        fakeGame.setMessage(faker.lorem().sentence());

        emitNext(fakeGame);
    }

    private void emitNext(Game game) {
        gameSink.tryEmitNext(game);
    }

    // http://localhost:8080/api/v1/games/stream
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Game> getGameUpdates() {
        return this.gameFlux;
    }
}
