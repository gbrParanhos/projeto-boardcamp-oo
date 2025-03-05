package com.boardcamp.api.dtos;

import java.time.LocalDate;

import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.models.GamesModel;

import lombok.Data;

@Data
public class RentalsDTO {

  private CustomersModel customer;

  private GamesModel game;

  private LocalDate rentDate;

  private Integer daysRented;

  private LocalDate returnDate;

  private Integer originalPrice;

  private Integer delayFee;
}