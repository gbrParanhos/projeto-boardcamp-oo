package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RentalsDTO {

  @NotNull
  private Long customerId;

  @NotNull
  private Long gameId;

  @Positive
  @NotNull
  private Integer daysRented;
}