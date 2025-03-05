package com.boardcamp.api.dtos;

import lombok.Data;

@Data
public class GamesDTO {

  private String name;

  private String image;

  private Integer stockTotal;

  private Integer pricePerDay;
}