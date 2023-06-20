package org.gvormbrock.carpark.mapper;

import org.gvormbrock.carpark.dto.TownDto;
import org.gvormbrock.carpark.model.Town;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public class TownToTownDtoMapperImpl implements TownToTownDtoMapper {

    public TownDto map(Town town) {
        TownDto townDto = new TownDto();
        long id = town.getId();
        townDto.setId(id);
        townDto.setName(town.getName());
        townDto.setCountryCode(town.getCountry().getCountryCode());
        return townDto;
    }
}
