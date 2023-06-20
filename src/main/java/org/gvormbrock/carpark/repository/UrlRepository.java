package org.gvormbrock.carpark.repository;

import org.gvormbrock.carpark.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    @Query("SELECT u FROM Url u JOIN u.town t JOIN t.country c WHERE c.countryCode = :countryCode AND t.name = :townName")
    List<Url> findByTownAndCountryCode(String townName, String countryCode);
}
