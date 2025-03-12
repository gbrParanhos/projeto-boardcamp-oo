package com.boardcamp.api.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rentals")
public class RentalsModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private LocalDate rentDate;

  @Column(nullable = false)
  private Integer daysRented;

  @Column(nullable = true)
  private LocalDate returnDate;

  @Column(nullable = false)
  private Integer originalPrice;

  @Column(nullable = false)
  private Integer delayFee;

  @ManyToOne
  @JoinColumn(name = "customerId")
  private CustomersModel customer;

  @ManyToOne
  @JoinColumn(name = "gameId")
  private GamesModel game;

  public RentalsModel(GamesModel game, CustomersModel customer, LocalDate date, Integer daysRented,
      Integer originalPrice) {
    this.customer = customer;
    this.game = game;
    this.rentDate = date;
    this.daysRented = daysRented;
    this.originalPrice = originalPrice;
    this.delayFee = 0;
  }
}