package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.services.CustomersService;

@SpringBootTest
class CustomersUnitTests {

	@InjectMocks
	private CustomersService customersService;

	@Mock
	private CustomersRepository customersRepository;

	// given when then

	@Test
	void givenNothing_whenFidingCustomers_thenListCustomers() {
		CustomersDTO dto = new CustomersDTO("Test", "11111111111", "11111111111");
		List<CustomersModel> customers = List.of(new CustomersModel(dto));
		doReturn(customers).when(customersRepository).findAll();

		List<CustomersModel> result = customersService.findCustomers();

		verify(customersRepository, times(1)).findAll();
		assertEquals(customers, result);
	}

	@Test
	void givenRepeatedCustomer_whenCreatingCustomer_thenThrowsError() {
		CustomersDTO dto = new CustomersDTO("Test", "11111111111", "11111111111");
		CustomersModel customer = new CustomersModel(dto);
		doReturn(true).when(customersRepository).existsByCpf(any());
		doReturn(customer).when(customersRepository).save(any());

		CustomException exception = assertThrows(CustomException.class, () -> customersService.saveCustomer(dto));

		verify(customersRepository, times(1)).existsByCpf(any());
		verify(customersRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("Já existe um cliente cadastrado com esse cpf.", exception.getMessage());
	}

	@Test
	void givenValidCustomer_whenCreatingCustomer_thenCreatesCustomer() {
		CustomersDTO dto = new CustomersDTO("Test", "11111111111", "11111111111");
		CustomersModel customer = new CustomersModel(dto);
		doReturn(false).when(customersRepository).existsByCpf(any());
		doReturn(customer).when(customersRepository).save(any());

		CustomersModel result = customersService.saveCustomer(dto);

		verify(customersRepository, times(1)).existsByCpf(any());
		verify(customersRepository, times(1)).save(any());
		assertEquals(customer, result);
	}
}
