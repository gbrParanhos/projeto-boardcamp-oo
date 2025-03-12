package com.boardcamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.boardcamp.api.dtos.GamesDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.services.GamesService;

@SpringBootTest
class GamesUnitTests {

	@InjectMocks
	private GamesService gamesService;

	@Mock
	private GamesRepository gamesRepository;

	// given when then

	@Test
	void givenNothing_whenFidingGames_thenListGames() {
		GamesDTO dto = new GamesDTO("Test", "Test", 1, 1);
		List<GamesModel> games = List.of(new GamesModel(dto));
		doReturn(games).when(gamesRepository).findAll();

		List<GamesModel> result = gamesService.findAll();

		verify(gamesRepository, times(1)).findAll();
		assertEquals(games, result);
	}

	@Test
	void givenRepeatedGame_whenCreatingGame_thenThrowsError() {
		GamesDTO dto = new GamesDTO("Test", "Test", 1, 1);
		GamesModel game = new GamesModel(dto);
		doReturn(Optional.of(game)).when(gamesRepository).findByName(any());

		CustomException exception = assertThrows(CustomException.class, () -> gamesService.saveGame(dto));

		verify(gamesRepository, times(1)).findByName(any());
		verify(gamesRepository, times(0)).save(any());
		assertNotNull(exception);
		assertEquals("Já existe um jogo com esse nome.", exception.getMessage());
	}

	@Test
	void givenValidGame_whenCreatingGame_thenCreatesGame() {
		GamesDTO dto = new GamesDTO("Test", "Test", 1, 1);
		GamesModel game = new GamesModel(dto);
		doReturn(Optional.empty()).when(gamesRepository).findByName(any());
		doReturn(game).when(gamesRepository).save(any());

		GamesModel result = gamesService.saveGame(dto);

		verify(gamesRepository, times(1)).findByName(any());
		verify(gamesRepository, times(1)).save(any());
		assertEquals(game, result);
	}
}
