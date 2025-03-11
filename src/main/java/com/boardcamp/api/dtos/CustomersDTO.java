package com.boardcamp.api.dtos;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {

  @NotBlank
  private String name;

  @Size(min = 10, max = 11)
  @NumberFormat
  private String phone;

  @Size(min = 11, max = 11)
  @NotBlank
  private String cpf;
}