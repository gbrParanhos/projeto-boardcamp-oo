package com.boardcamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boardcamp.api.dtos.CustomersDTO;
import com.boardcamp.api.exceptions.CustomException;
import com.boardcamp.api.models.CustomersModel;
import com.boardcamp.api.repositories.CustomersRepository;

@Service
public class CustomersService {

  final CustomersRepository customersRepository;

  CustomersService(CustomersRepository customersRepository) {
    this.customersRepository = customersRepository;
  }

  public List<CustomersModel> findCustomers() {
    return customersRepository.findAll();
  }

  public CustomersModel findCustomerById(Long id) {
    return customersRepository.findById(id).orElseThrow(
        () -> new CustomException("notFound", "Não foi encontrado um cliente com este id."));
  }

  public CustomersModel saveCustomer(CustomersDTO dto) {
    if (customersRepository.existsByCpf(dto.getCpf())) {
      throw new CustomException("conflict", "Já existe um cliente cadastrado com esse cpf.");
    }
    CustomersModel customer = new CustomersModel(dto);
    return customersRepository.save(customer);
  }

}
