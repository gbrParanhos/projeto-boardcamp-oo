package com.boardcamp.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.GamesModel;

@Repository
public interface GamesRepository extends JpaRepository<GamesModel, Long> {

  boolean existsByName(String name);

  Optional<GamesModel> findByName(String name);
}
