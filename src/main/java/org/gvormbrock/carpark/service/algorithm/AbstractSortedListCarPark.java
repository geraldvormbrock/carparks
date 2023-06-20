package org.gvormbrock.carpark.service.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.dto.GeoLocation;
import org.gvormbrock.carpark.model.CarPark;

import java.util.List;
import java.util.Map;

/**
 * Extend this class to implement a new algorithm or extend AbstractSortedListCarParkTowns
 */
public abstract class AbstractSortedListCarPark {
    // Existing car parks in the DB
    Map<String, CarPark> hashMapExistingCarPark = null;

    /**
     * Must load or update hashMapExistingCarPark
     *
     * @throws JsonProcessingException if any json exception occurs
     */
    public abstract void loadCarParksInDB() throws JsonProcessingException;

    /**
     * Provide the full car-park list ordered from nearest to farthest.
     * For that, the method can use the town URLs of web services.
     *
     * @param comparedGeoLocation The position to be compared to the car-parks locations.
     * @return The ordered car-park list.
     *
     * @throws JsonProcessingException if any Json exception occurs
     */
    public abstract List<CarParkDto> provideListOrderedByNearest(GeoLocation comparedGeoLocation) throws JsonProcessingException;

}
