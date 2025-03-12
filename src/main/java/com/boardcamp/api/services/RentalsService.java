package com.boardcamp.api.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;
import com.boardcamp.api.models.RentalsModel;
import com.boardcamp.api.repositories.CustomersRepository;
import com.boardcamp.api.repositories.GamesRepository;
import com.boardcamp.api.repositories.RentalsRepository;

@Service
public class RentalsService {

  final GamesRepository gamesRepository;
  final CustomersRepository customersRepository;
  final RentalsRepository rentalsRepository;

  RentalsService(GamesRepository gamesRepository,
      CustomersRepository customersRepository,
      RentalsRepository rentalsRepository) {
    this.gamesRepository = gamesRepository;
    this.customersRepository = customersRepository;
    this.rentalsRepository = rentalsRepository;
  }

  public List<RentalsModel> findRentals() {
    return rentalsRepository.findAll();
  }

  public RentalsModel saveRental(RentalsDTO dto) {
    CustomersModel customer = customersRepository
        .findById(dto.getCustomerId())
        .orElseThrow(() -> new CustomException("notFound", "Não foi encontrado um cliente com este id."));

    GamesModel game = gamesRepository
        .findById(dto.getGameId())
        .orElseThrow(() -> new CustomException("notFound", "Não foi encontrado um jogo com este id."));

    Integer currentRentals = rentalsRepository.findCurrentRentals(dto.getGameId()).get(0).get(1);

    if (game.getStockTotal() <= currentRentals) {
      throw new CustomException("unprocessableEntity", "Não há estoque para alugar este jogo no momento.");
    }

    LocalDate date = LocalDate.now();
    Integer daysRented = dto.getDaysRented();
    Integer originalPrice = game.getPricePerDay() * daysRented;

    RentalsModel rental = new RentalsModel(game, customer, date, daysRented, originalPrice);
    return rentalsRepository.save(rental);
  }

  public RentalsModel saveReturn(Long id) {
    RentalsModel rental = rentalsRepository
        .findById(id)
        .orElseThrow(() -> new CustomException("notFound", "Não foi encontrado um aluguel com este id."));
    if (rental.getReturnDate() != null) {
      throw new CustomException("unprocessableEntity", "Esse aluguel já foi devolvido.");
    }
    rental.setReturnDate(LocalDate.now());
    Integer totalPeriod = Math.toIntExact(ChronoUnit.DAYS.between(rental.getRentDate(), rental.getReturnDate()));
    if (totalPeriod - rental.getDaysRented() > 0) {
      rental.setDelayFee(rental.getGame().getPricePerDay() * (totalPeriod - rental.getDaysRented()));
    }
    return rentalsRepository.save(rental);
  }

  public void removeRental(Long id) {
    RentalsModel rental = rentalsRepository
        .findById(id)
        .orElseThrow(() -> new CustomException("notFound", "Não foi encontrado um aluguel com este id."));
    if (rental.getReturnDate() == null) {
      throw new CustomException("badRequest", "Esse aluguel não foi devolvido.");
    }
    rentalsRepository.deleteById(id);
  }
}