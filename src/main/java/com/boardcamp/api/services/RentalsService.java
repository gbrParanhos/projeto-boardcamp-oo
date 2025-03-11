package com.boardcamp.api.services;

import org.springframework.stereotype.Service;

import com.boardcamp.api.repositories.RentalsRepository;

@Service
public class RentalsService {

  final RentalsRepository rentalsRepository;

  RentalsService(RentalsRepository rentalsRepository) {
    this.rentalsRepository = rentalsRepository;
  }
}
