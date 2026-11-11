package com.tripsafe.repository;

import com.tripsafe.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findAllByOrderByCreatedAtDesc();
    List<Trip> findByDepartureDateGreaterThanEqualOrderByDepartureDateAsc(LocalDate date);
}
