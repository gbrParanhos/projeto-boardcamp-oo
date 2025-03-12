package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GamesIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private GamesRepository gamesRepository;

  @BeforeEach
  @AfterEach
  void cleanUpDatabase() {
    gamesRepository.deleteAll();
  }

  @Test
  void givenNothing_whenFidingGames_thenListGames() {
    GamesModel game = new GamesModel(null, "testName", "test", 1, 1000);
    gamesRepository.save(game);

    ResponseEntity<List<GamesModel>> response = restTemplate.exchange("/games", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<GamesModel>>() {
        });

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(List.of(game), response.getBody());
    assertEquals(1, gamesRepository.count());
  }

  @Test
  void givenRepeatedGame_whenCreatingGame_thenThrowsError() {
    GamesModel game = new GamesModel(null, "RepeatedName", "test", 1, 1000);
    gamesRepository.save(game);

    GamesDTO dto = new GamesDTO("RepeatedName", "test2", 2, 2000);
    HttpEntity<GamesDTO> body = new HttpEntity<>(dto);
    ResponseEntity<String> response = restTemplate.exchange("/games", HttpMethod.POST, body, String.class);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("Já existe um jogo com esse nome.", response.getBody());
    assertEquals(1, gamesRepository.count());
  }

  @Test
  void givenValidGame_whenCreatingGame_thenCreatesGame() {

    GamesDTO dto = new GamesDTO("testName", "test2", 2, 2000);
    HttpEntity<GamesDTO> body = new HttpEntity<>(dto);
    ResponseEntity<GamesModel> response = restTemplate.exchange("/games",
        HttpMethod.POST, body, GamesModel.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(new GamesModel(dto).getName(), response.getBody().getName());
    assertEquals(new GamesModel(dto).getImage(), response.getBody().getImage());
    assertEquals(new GamesModel(dto).getStockTotal(), response.getBody().getStockTotal());
    assertEquals(new GamesModel(dto).getPricePerDay(), response.getBody().getPricePerDay());
    assertEquals(1, gamesRepository.count());
  }

}
