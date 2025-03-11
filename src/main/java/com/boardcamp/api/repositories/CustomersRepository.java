package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.CustomersModel;

@Repository
public interface CustomersRepository extends JpaRepository<CustomersModel, Long> {

  boolean existsByCpf(String cpf);

  Optional<CustomersModel> findByCpf(String cpf);
}
