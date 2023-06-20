package org.gvormbrock.carpark.repository;

import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarParkRepository extends JpaRepository<CarPark, Long> {
    @Query("SELECT cp FROM CarPark cp JOIN cp.town t JOIN t.country c WHERE c.countryCode = :countryCode AND t.name = :townName")
    List<CarPark> findByTownAndCountryCode(String townName, String countryCode);

}
