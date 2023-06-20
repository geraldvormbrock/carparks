package org.gvormbrock.carpark.mapper;

import org.gvormbrock.carpark.dto.TownDto;
import org.gvormbrock.carpark.model.Town;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TownToTownDtoMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "country.name", target = "country")
    TownDto map(Town town);
}
