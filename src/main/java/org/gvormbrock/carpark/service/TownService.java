package org.gvormbrock.carpark.service;

import lombok.RequiredArgsConstructor;
import org.gvormbrock.carpark.dto.TownDto;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.mapper.TownToTownDtoMapper;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.model.Url;
import org.gvormbrock.carpark.repository.TownRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TownService {
    private final TownRepository townRepository;
    private final TownToTownDtoMapper townToTownDtoMapper;
    public Optional<Town> findByNameAndCountryCode(String townName, String countryCode) {
        return townRepository.findByNameAndCountryCode(townName, countryCode);
    }

    public Optional<Town> findById(Long id) {
        return townRepository.findById(id);
    }

    public List<TownDto> findAll() {
        return townRepository.findAll().stream()
                .map(townToTownDtoMapper::map).toList();
    }

    public void save(Town url) {
        townRepository.save(url);
    }
}
