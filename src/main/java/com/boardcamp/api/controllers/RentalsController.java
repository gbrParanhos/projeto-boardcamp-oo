package com.boardcamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.boardcamp.api.dtos.RentalsDTO;
import com.boardcamp.api.services.RentalsService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/rentals")
public class RentalsController {
  final RentalsService rentalsService;

  RentalsController(RentalsService rentalsService) {
    this.rentalsService = rentalsService;
  }

  @GetMapping
  public ResponseEntity<Object> getRentals() {
    return ResponseEntity.status(HttpStatus.OK).body(rentalsService.findRentals());
  }

  @PostMapping
  public ResponseEntity<Object> postRental(@RequestBody @Valid RentalsDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(rentalsService.saveRental(dto));
  }

  @PostMapping("/{id}/return")
  public ResponseEntity<Object> postReturn(@PathVariable("id") Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(rentalsService.saveReturn(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteRental(@PathVariable("id") Long id) {
    rentalsService.removeRental(id);
    return ResponseEntity.status(HttpStatus.OK).body(null);
  }

}
