package com.boardcamp.api.controllers;

import com.boardcamp.api.services.RentalsService;

public class RentalsController {
  final RentalsService rentalsService;

  RentalsController(RentalsService rentalsService) {
    this.rentalsService = rentalsService;
  }
}
