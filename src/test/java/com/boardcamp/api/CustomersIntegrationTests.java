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

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomersIntegrationTests {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private CustomersRepository customersRepository;

  @BeforeEach
  @AfterEach
  void cleanUpDatabase() {
    customersRepository.deleteAll();
  }

  @Test
  void givenNothing_whenFidingCustomers_thenListCustomers() {
    CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
    customersRepository.save(customer);

    ResponseEntity<List<CustomersModel>> response = restTemplate.exchange("/customers", HttpMethod.GET, null,
        new ParameterizedTypeReference<List<CustomersModel>>() {
        });

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(List.of(customer), response.getBody());
    assertEquals(1, customersRepository.count());
  }

  @Test
  void givenRepeatedCustomer_whenCreatingCustomer_thenThrowsError() {
    CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
    customersRepository.save(customer);

    CustomersDTO dto = new CustomersDTO("Test", "11111111111", "11111111111");
    HttpEntity<CustomersDTO> body = new HttpEntity<>(dto);
    ResponseEntity<String> response = restTemplate.exchange("/customers", HttpMethod.POST, body, String.class);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("Já existe um cliente cadastrado com esse cpf.", response.getBody());
    assertEquals(1, customersRepository.count());
  }

  @Test
  void givenValidCustomer_whenCreatingCustomer_thenCreatesCustomer() {

    CustomersDTO dto = new CustomersDTO("Test", "11111111111", "11111111111");
    HttpEntity<CustomersDTO> body = new HttpEntity<>(dto);
    ResponseEntity<CustomersModel> response = restTemplate.exchange("/customers",
        HttpMethod.POST, body, CustomersModel.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(new CustomersModel(dto).getName(), response.getBody().getName());
    assertEquals(new CustomersModel(dto).getPhone(), response.getBody().getPhone());
    assertEquals(new CustomersModel(dto).getCpf(), response.getBody().getCpf());
    assertEquals(1, customersRepository.count());
  }

}
