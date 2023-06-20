package org.gvormbrock.carpark.mapper;

import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.model.CarPark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CarParkToCarParkDtoMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "longitude", target = "location.longitude")
    @Mapping(source = "latitude", target = "location.latitude")
    CarParkDto map(CarPark carPark);
}
