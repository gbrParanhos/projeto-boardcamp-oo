package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;

@Service
public class GamesService {
  final GamesRepository gamesRepository;

  GamesService(GamesRepository gamesRepository) {
    this.gamesRepository = gamesRepository;
  }

  public List<GamesModel> findAll() {
    return gamesRepository.findAll();
  }

  public GamesModel saveGame(GamesDTO dto) {
    if (gamesRepository.findByName(dto.getName()).isPresent()) {
      throw new CustomException("conflict", "Já existe um jogo com esse nome.");
    }
    GamesModel game = new GamesModel(dto);
    return gamesRepository.save(game);
  }
}