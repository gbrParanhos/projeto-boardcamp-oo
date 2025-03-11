package com.boardcamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomersDTO {

  @NotBlank
  private String name;

  @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números e ter 10 ou 11 dígitos.")
  private String phone;

  @Size(min = 11, max = 11)
  @NotBlank
  private String cpf;
}