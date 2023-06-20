package org.gvormbrock.carpark.service;

import lombok.RequiredArgsConstructor;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.model.Url;
import org.gvormbrock.carpark.repository.CarParkRepository;
import org.gvormbrock.carpark.repository.TownRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarParkService {
    private final CarParkRepository carParkRepository;
    private final TownRepository townRepository;

    public Optional<CarPark> findById(Long id) {
        return carParkRepository.findById(id);
    }

    public List<CarPark> findByTownAndCountryCode(String townName, String countryCode) {
        return carParkRepository.findByTownAndCountryCode(townName, countryCode);
    }

    public void save(CarPark carPark) {
        carParkRepository.save(carPark);
    }

    public void delete(CarPark carPark) {
        carParkRepository.delete(carPark);
    }

    public List<CarPark> findAll() {
        return carParkRepository.findAll();
    }

}
