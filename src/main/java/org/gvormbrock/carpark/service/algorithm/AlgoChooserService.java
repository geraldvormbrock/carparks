package org.gvormbrock.carpark.service.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.dto.GeoLocation;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.ErrorServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlgoChooserService {
    public static final int STOP_MAX_FACTOR = 30;
    @Autowired
    private final List<AbstractSortedListCarParkTowns> algorithmList;

    public List<CarParkDto> listLimitCarParks(String town, String countryCode, GeoLocation geoLocation, int nbFreeCarPark) throws JsonProcessingException {
        AbstractSortedListCarParkTowns algo = findRightAlgorithmClass(town, countryCode);
        List<CarParkDto> sortedCarParks = algo.provideListOrderedByNearest(geoLocation);

        // Add car parks if some are full to get a list with N free car parks (N = inputDto.getNbFreeCarPark())
        long nbReturnedCarParks = nbFreeCarPark;
        long nbFullCarParcs;
        do {
            nbFullCarParcs = sortedCarParks.stream().limit(nbReturnedCarParks).filter(x->x.getNbFreeLocations() == 0).count();
            nbReturnedCarParks += nbFullCarParcs;
        } while (nbFullCarParcs > 0 && nbReturnedCarParks < nbFreeCarPark * STOP_MAX_FACTOR);
        return sortedCarParks.stream().limit(nbReturnedCarParks).toList();
    }

    private AbstractSortedListCarParkTowns findRightAlgorithmClass(String town, String countryCode) {
        List<AbstractSortedListCarParkTowns> candidates = algorithmList.stream().filter(x -> town.equals(x.getTownName()) && countryCode.equals(x.getCountryCode())).toList();
        if (candidates.isEmpty()) {
            throw new ErrorServerException(ErrorCode.TOWN_NOT_FOUND, "The town " + town + " in country " + countryCode + " is not supported.");
        }
        if (candidates.size() > 1) {
            throw new ErrorServerException(ErrorCode.SERVER_ERROR, "The town " + town + " in country " + countryCode + " uses more than one algorithm.");
        }
        return candidates.get(0);
    }
}
