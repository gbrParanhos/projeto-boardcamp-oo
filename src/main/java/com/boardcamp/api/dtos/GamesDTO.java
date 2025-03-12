package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamesDTO {

  @NotBlank
  private String name;

  private String image;

  @Positive
  @NotNull
  private Integer stockTotal;

  @Positive
  @NotNull
  private Integer pricePerDay;
}