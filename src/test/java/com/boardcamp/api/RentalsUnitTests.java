package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;
import com.boardcamp.api.services.RentalsService;

@SpringBootTest
class RentalsUnitTests {

	@InjectMocks
	private RentalsService rentalsService;

	@Mock
	private RentalsRepository rentalsRepository;

	@Mock
	private CustomersRepository customersRepository;

	@Mock
	private GamesRepository gamesRepository;

	// given when then

	@Test
	void givenNotFoundCustomerId_whenCreatingRental_thenThrowsError() {
		doReturn(Optional.empty()).when(customersRepository).findById(any());

		RentalsDTO dto = new RentalsDTO(1L, 1L, 1);
		CustomException exception = assertThrows(CustomException.class, () -> rentalsService.saveRental(dto));

		verify(customersRepository, times(1)).findById(any());
		verify(gamesRepository, times(0)).findById(any());
		verify(rentalsRepository, times(0)).findCurrentRentals(any());
		verify(rentalsRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("Não foi encontrado um cliente com este id.",
				exception.getMessage());
	}

	@Test
	void givenNotFoundGameId_whenCreatingRental_thenThrowsError() {
		CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
		doReturn(Optional.of(customer)).when(customersRepository).findById(any());
		doReturn(Optional.empty()).when(gamesRepository).findById(any());

		RentalsDTO dto = new RentalsDTO(1L, 1L, 1);
		CustomException exception = assertThrows(CustomException.class, () -> rentalsService.saveRental(dto));

		verify(customersRepository, times(1)).findById(any());
		verify(gamesRepository, times(1)).findById(any());
		verify(rentalsRepository, times(0)).findCurrentRentals(any());
		verify(rentalsRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("Não foi encontrado um jogo com este id.",
				exception.getMessage());
	}

	@Test
	void givenValidRental_whenCreatingRental_thenCreatesRental() {
		CustomersModel customer = new CustomersModel(null, "Test", "11111111111", "11111111111");
		doReturn(Optional.of(customer)).when(customersRepository).findById(any());

		GamesModel game = new GamesModel(null, "Test", "Test", 1, 1000);
		doReturn(Optional.of(game)).when(gamesRepository).findById(any());

		RentalsModel rental = new RentalsModel(null, LocalDate.now(), 3, null, 3000, 0, customer, game);
		doReturn(List.of(List.of(1, 0))).when(rentalsRepository).findCurrentRentals(any());
		doReturn(rental).when(rentalsRepository).save(any());

		RentalsModel result = rentalsService.saveRental(new RentalsDTO(1L, 1L, 1));

		verify(gamesRepository, times(1)).findById(any());
		verify(customersRepository, times(1)).findById(any());
		verify(rentalsRepository, times(1)).findCurrentRentals(any());
		verify(rentalsRepository, times(1)).save(any());
		assertEquals(rental, result);
	}
}
