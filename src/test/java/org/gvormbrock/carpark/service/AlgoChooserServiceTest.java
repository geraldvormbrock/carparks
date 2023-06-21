package org.gvormbrock.carpark.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.dto.GeoLocation;
import org.gvormbrock.carpark.exception.ErrorServerException;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.service.algorithm.AlgoChooserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class AlgoChooserServiceTest {
    @Autowired
    private AlgoChooserService algoChooserService;

    @Test
    void testWrongTownNameThrowsException() {
        Assertions.assertThrows(ErrorServerException.class, () -> algoChooserService.listLimitCarParks("Unknown town", "fr", new GeoLocation(0.0, 0.0D), 1));
    }

    @Test
    void testWrongCountryCodeThrowsException() {
        Assertions.assertThrows(ErrorServerException.class, () -> algoChooserService.listLimitCarParks("Poitiers", "xx", new GeoLocation(0.0, 0.0D), 1));
    }

    @Test
    void testListLimitCarParks() throws JsonProcessingException {
        List<CarParkDto> ret = algoChooserService.listLimitCarParks("Poitiers", "fr", new GeoLocation(46.58349874703973, 0.3450022616476489), 1);
        Assertions.assertEquals("NOTRE DAME", ret.get(0).getName());
    }

}
