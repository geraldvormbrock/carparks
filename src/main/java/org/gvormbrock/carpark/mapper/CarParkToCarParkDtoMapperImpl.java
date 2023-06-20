package org.gvormbrock.carpark.mapper;

import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.model.CarPark;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public class CarParkToCarParkDtoMapperImpl implements CarParkToCarParkDtoMapper {
    public CarParkDto map(CarPark carPark) {
        CarParkDto carParkDto = new CarParkDto();
        carParkDto.setName(carPark.getName());
        carParkDto.getGeoLocation().setLongitude(carPark.getLongitude());
        carParkDto.getGeoLocation().setLatitude(carPark.getLatitude());
        return carParkDto;
    }
}
