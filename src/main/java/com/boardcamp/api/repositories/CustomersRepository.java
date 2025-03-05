package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.CustomersModel;

@Repository
public interface CustomersRepository extends JpaRepository<CustomersModel, Long> {

  boolean existsByName(String name);

  Optional<CustomersModel> findByName(String name);
}
