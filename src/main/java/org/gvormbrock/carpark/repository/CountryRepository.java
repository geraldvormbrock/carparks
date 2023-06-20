package org.gvormbrock.carpark.repository;

import org.gvormbrock.carpark.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);

    Optional<Country> findByCountryCode(String countryCode);
}
