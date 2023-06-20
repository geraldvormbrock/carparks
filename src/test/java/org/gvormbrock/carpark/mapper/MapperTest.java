package org.gvormbrock.carpark.mapper;

import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Country;
import org.gvormbrock.carpark.model.Town;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapperTest {

    @Test
    void testCarParkToCarParkDtoMapper() {
        CarParkToCarParkDtoMapper carParkToCarParkDtoMapper = new CarParkToCarParkDtoMapperImpl();
        Country country = Country.builder().name("COUNTRY").countryCode("fr").build();
        Town town = Town.builder().name("TOWN").country(country).build();
        CarPark carPark = new CarPark(1L, town, "CAR PARK NAME", 0.1, 0.2);
        CarParkDto carParkDto = carParkToCarParkDtoMapper.map(carPark);
        Assertions.assertEquals("CAR PARK NAME", carParkDto.getName());
        Assertions.assertEquals(0.1, carParkDto.getGeoLocation().getLatitude());
        Assertions.assertEquals(0.2, carParkDto.getGeoLocation().getLongitude());
    }
}
