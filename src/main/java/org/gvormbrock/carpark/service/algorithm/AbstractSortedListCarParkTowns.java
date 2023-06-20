package org.gvormbrock.carpark.service.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.ErrorServerException;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.mapper.CarParkToCarParkDtoMapper;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.service.CarParkService;
import org.gvormbrock.carpark.service.TownService;
import org.gvormbrock.carpark.service.UrlService;
import org.gvormbrock.carpark.service.WebServiceCaller;
import org.gvormbrock.carpark.util.CollectionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Extend this class to create an algorithm depending on town
 */
@Getter
@Setter
@RequiredArgsConstructor
public abstract class AbstractSortedListCarParkTowns extends AbstractSortedListCarPark {
    private static final Logger LOGGER = LogManager.getLogger(AbstractSortedListCarParkTowns.class);

    @Autowired
    protected WebServiceCaller webServiceCaller;

    @Autowired
    protected TownService townService;

    @Autowired
    protected UrlService urlService;

    @Autowired
    protected CarParkService carParkService;

    @Autowired
    protected CarParkToCarParkDtoMapper carParkToCarParkDtoMapper;

    // Those attributes must be instantiated in the constructor depending on the algorithm
    protected String countryCode;
    protected String townName;

    @Transactional
    public Map<String, CarPark> getHashMapExistingCarPark() throws JsonProcessingException {
        if (hashMapExistingCarPark == null) {
            loadCarParksInDB();
        }
        return hashMapExistingCarPark;
    }

    protected static JsonNode getJsonNode(Integer recordNum, JsonNode jsonRecord, String name) {
        JsonNode jsonFields = jsonRecord.get(name);
        if (jsonFields == null) {
            throw new ErrorServerException(ErrorCode.JSON_MAL_FORMATTED,
                    name + " can not be found in " + (recordNum == null ? "json" : "json record " + recordNum));
        }
        return jsonFields;
    }


    protected void deleteCarParksIfRemoved(Map<String, CarPark> hashMapRetrievedCarPark) {
        if (!hashMapRetrievedCarPark.isEmpty()) {
            Set<?> difference = CollectionsUtil.difference(hashMapExistingCarPark.values(), hashMapRetrievedCarPark.values());
            if (!difference.isEmpty()) {
                for (Object obj : difference) {
                    CarPark carPark = (CarPark) obj;
                    LOGGER.info(" Delete car park '" + carPark.getName() + "'");
                    carParkService.delete(carPark);
                }
            }
        }
    }

    protected void addOrUpdateCarParkIfNeeded(CarPark carPark) throws JsonProcessingException {
        CarPark oldCarPark = getHashMapExistingCarPark().get(carPark.getName());
        if (oldCarPark == null) {
            // A new car park have been constructed, add it
            LOGGER.info("Add car park '" + carPark.getName() + "'");
            carParkService.save(carPark);
            hashMapExistingCarPark.put(carPark.getName(), carPark);
        } else if (!carPark.equals(oldCarPark)) {
            // A car park have been updated
            carPark.setId(oldCarPark.getId());
            carPark.setTown(oldCarPark.getTown());
            LOGGER.info("Car park '" + carPark.getName() + "' updated");
            carParkService.save(carPark);
            hashMapExistingCarPark.put(carPark.getName(), carPark);
        }
    }

    protected Town findTown() {
        Optional<Town> townOptional = townService.findByNameAndCountryCode(townName, countryCode);
        if (townOptional.isEmpty()) {
            throw new NotFoundException(ErrorCode.TOWN_NOT_FOUND, "The town '" + townName + " of country " + countryCode + " does not exist");
        }
        return townOptional.get();
    }
}
