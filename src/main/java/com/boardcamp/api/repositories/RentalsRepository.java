package com.boardcamp.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.boardcamp.api.models.RentalsModel;

@Repository
public interface RentalsRepository extends JpaRepository<RentalsModel, Long> {

  @Query(nativeQuery = true, value = "SELECT games.\"stock_total\", COUNT(rentals.id) AS \"rentals_total\" FROM games" +
      " LEFT JOIN rentals ON games.id = rentals.\"game_id\" AND rentals.\"return_date\" IS null" +
      " WHERE games.id = :gameId" +
      " GROUP BY games.id;")
  List<List<Integer>> findCurrentRentals(@Param("gameId") Long gameId);

}
