package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.services.GamesService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/games")
public class GamesController {

  final GamesService gamesService;

  GamesController(GamesService gamesService) {
    this.gamesService = gamesService;
  }

  @GetMapping
  public ResponseEntity<Object> getGames() {
    return ResponseEntity.status(HttpStatus.OK).body(gamesService.findAll());
  }

  @PostMapping
  public ResponseEntity<Object> postGame(@RequestBody @Valid GamesDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(gamesService.saveGame(dto));
  }

}
