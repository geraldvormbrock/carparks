package org.gvormbrock.carpark.service;

import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CarParkServiceTest {
    @Autowired
    private CarParkService carParkService;

    @Autowired
    private TownService townService;

    @Test
    void testSaveDeleteFindBtTownAndCountryCode() {
        Optional<Town> town = townService.findByNameAndCountryCode("TEST", "fr");
        if (town.isEmpty()) {
            throw new RuntimeException("Town TEST not found in country fr");
        }
        CarPark carPark = new CarPark(null, town.get(), "CAR PARK TEST 1", 0.0, 0.0);
        carParkService.save(carPark);
        carPark = new CarPark(null, town.get(), "CAR PARK TEST 2", 0.0, 0.0);
        carParkService.save(carPark);
        List<CarPark> carParks = carParkService.findByTownAndCountryCode("TEST", "fr");
        Assertions.assertNotNull(carParks);
        Assertions.assertEquals(2, carParks.size());
        carParkService.delete(carParks.stream().filter(x->"CAR PARK TEST 2".equals(x.getName())).toList().get(0));
        carParks = carParkService.findByTownAndCountryCode("TEST", "fr");
        Assertions.assertEquals(1, carParks.size());
        Assertions.assertEquals("CAR PARK TEST 1", carParks.get(0).getName());
    }
}
