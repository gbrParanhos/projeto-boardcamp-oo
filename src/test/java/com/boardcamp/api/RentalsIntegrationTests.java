package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RentalsIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private RentalsRepository rentalsRepository;

  @Autowired
  private CustomersRepository customersRepository;

  @Autowired
  private GamesRepository gamesRepository;

  @BeforeEach
  @AfterEach
  void cleanUpDatabase() {
    rentalsRepository.deleteAll();
    customersRepository.deleteAll();
    gamesRepository.deleteAll();
  }

  @Test
  void givenNotFoundCustomerId_whenCreatingRental_thenThrowsError() {
    GamesModel game = new GamesModel(null, "Test", "Test", 1, 1000);
    gamesRepository.save(game);

    RentalsDTO dto = new RentalsDTO(1L, game.getId(), 3);
    HttpEntity<RentalsDTO> body = new HttpEntity<>(dto);
    ResponseEntity<String> response = restTemplate.exchange("/rentals",
        HttpMethod.POST, body, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Não foi encontrado um cliente com este id.",
        response.getBody());
    assertEquals(0, customersRepository.count());
    assertEquals(1, gamesRepository.count());
    assertEquals(0, rentalsRepository.count());
  }

  @Test
  void givenNotFoundGameId_whenCreatingRental_thenThrowsError() {
    CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
    customersRepository.save(customer);

    RentalsDTO dto = new RentalsDTO(customer.getId(), 1L, 3);
    HttpEntity<RentalsDTO> body = new HttpEntity<>(dto);
    ResponseEntity<String> response = restTemplate.exchange("/rentals",
        HttpMethod.POST, body, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Não foi encontrado um jogo com este id.",
        response.getBody());
    assertEquals(1, customersRepository.count());
    assertEquals(0, gamesRepository.count());
    assertEquals(0, rentalsRepository.count());
  }

  @Test
  void givenValidRental_whenCreatingRental_thenCreatesRental() {
    CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
    GamesModel game = new GamesModel(null, "Test", "Test", 1, 1000);
    customersRepository.save(customer);
    gamesRepository.save(game);

    RentalsDTO dto = new RentalsDTO(customer.getId(), game.getId(), 3);
    HttpEntity<RentalsDTO> body = new HttpEntity<>(dto);
    ResponseEntity<RentalsModel> response = restTemplate.exchange("/rentals", HttpMethod.POST, body,
        RentalsModel.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    RentalsModel rental = new RentalsModel(null, LocalDate.now(), 3, null, 3000, 0, customer, game);
    assertEquals(rental.getCustomer(), response.getBody().getCustomer());
    assertEquals(rental.getGame(), response.getBody().getGame());
    assertEquals(rental.getDaysRented(), response.getBody().getDaysRented());
    assertEquals(rental.getDelayFee(), response.getBody().getDelayFee());
    assertEquals(rental.getOriginalPrice(), response.getBody().getOriginalPrice());
    assertEquals(rental.getRentDate(), response.getBody().getRentDate());
    assertEquals(rental.getReturnDate(), response.getBody().getReturnDate());
    assertEquals(1, customersRepository.count());
    assertEquals(1, gamesRepository.count());
    assertEquals(1, rentalsRepository.count());
  }

}
