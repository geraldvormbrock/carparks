package org.gvormbrock.carpark.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.dto.InputDto;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.service.CarParkService;
import org.gvormbrock.carpark.service.algorithm.AlgoChooserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CarParkController {
    private final CarParkService carParkService;
    private final AlgoChooserService algoChooserService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/car-parks-from-position")
    public List<CarParkDto> listCarParksFromPosition(@RequestBody InputDto inputDto) throws JsonProcessingException {
        return algoChooserService.listLimitCarParks(inputDto.getTownName(), inputDto.getCountryCode(),
                inputDto.getGeoLocation(), inputDto.getNbFreeCarPark());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/car-parks")
    public List<CarPark> listCarParks() {
        return carParkService.findAll();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/car-park/{id}")
    public Optional<CarPark> findCarParkById(@PathVariable long id) {
        return carParkService.findById(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/car-park/{id}")
    void deleteCarPark(@PathVariable Long id) {
        Optional<CarPark> cp = carParkService.findById(id);
        if (cp.isEmpty()) {
            throw new NotFoundException(ErrorCode.CAR_PARK_NOT_FOUND, "The car park with id = " + id + " does not exist");
        }
        carParkService.delete(cp.get());
    }
}
