package org.gvormbrock.carpark.repository;

import org.gvormbrock.carpark.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Long> {

    @Query("SELECT t FROM Town t JOIN t.country c WHERE c.countryCode = :countryCode AND t.name = :townName")
    Optional<Town> findByNameAndCountryCode(String townName, String countryCode);
}
