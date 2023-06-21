package org.gvormbrock.carpark.service.algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gvormbrock.carpark.dto.CarParkDto;
import org.gvormbrock.carpark.dto.GeoLocation;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.ErrorServerException;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.model.CarPark;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.model.Url;
import org.gvormbrock.carpark.util.GeoUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
public class SortedListCarParkPoitiers extends AbstractSortedListCarParkTowns {
    private static final Logger LOGGER = LogManager.getLogger(SortedListCarParkPoitiers.class);

    private String urlCarParkLocation = null;
    private String urlCarParkInformation = null;
    public SortedListCarParkPoitiers() {
        countryCode = "fr";
        townName = "Poitiers";
    }

    private void updateUrls() {
        @NotNull List<Url> urls = urlService.findByTownAndCountryCode(townName, countryCode);
        if (urls.size() != 2) {
            throw new NotFoundException(ErrorCode.SERVER_ERROR, "The town '" + townName + "' does not contain 2 URLs");
        }
        urlCarParkLocation = urls.stream().filter(url -> url.getTitle().equalsIgnoreCase("car park location")).toList().get(0).getUrlString();
        urlCarParkInformation = urls.stream().filter(url -> url.getTitle().equalsIgnoreCase("car park information")).toList().get(0).getUrlString();
    }

    public String getUrCarParkLocation() {
        if (urlCarParkLocation == null) {
            updateUrls();
        }
        return urlCarParkLocation;
    }

    public String getUrlCarParkInformation() {
        if (urlCarParkInformation == null) {
            updateUrls();
        }
        return urlCarParkInformation;
    }

    @Override
    @Scheduled(cron = "* 0/30 * * * ?") // Launch every 30 minutes. In e real project do it at night.
    public void loadCarParksInDB() throws JsonProcessingException {
        String url = getUrCarParkLocation();
        String jsonCarParkLocation = webServiceCaller.get(url);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNodeCarParkLocation = objectMapper.readTree(jsonCarParkLocation);
        JsonNode jsonRecords = jsonNodeCarParkLocation.get("records");

        if (jsonRecords != null && jsonRecords.isArray()) {

            // Save car parks in a hash map to see if new car parks have been added or if some have been modified
            List<CarPark> carParks = carParkService.findByTownAndCountryCode(townName, countryCode);
            hashMapExistingCarPark = new HashMap<>();
            for (CarPark carPark : carParks) {
                hashMapExistingCarPark.put(carPark.getName(), carPark);
            }
            Map<String, CarPark> hashMapRetrievedCarPark = new HashMap<>();
            int recordNum = 0;
            for (JsonNode jsonRecord : jsonRecords) {
                CarPark carPark = new CarPark();
                JsonNode jsonFields = getJsonNode(recordNum, jsonRecord, "fields");
                if (jsonFields == null) {
                    continue;
                }
                JsonNode jsonName = getJsonNode(recordNum, jsonFields, "nom");
                if (jsonName == null) {
                    continue;
                }
                carPark.setName(jsonName.asText().toUpperCase());
                GeoLocation carParkGeoLocation = parseJsonGeoPoint2D(jsonFields, recordNum);
                if (carParkGeoLocation == null) {
                    LOGGER.error("fields.geo_point_2d can not be loaded in record " + recordNum + " in url=" + url);
                    continue;
                }
                carPark.setLongitude(carParkGeoLocation.getLongitude());
                carPark.setLatitude(carParkGeoLocation.getLatitude());
                Town town = findTown();
                carPark.setTown(town);
                hashMapRetrievedCarPark.put(carPark.getName(), carPark);

                //
                addOrUpdateCarParkIfNeeded(carPark);
            }
            // See if car parks have been deleted
            deleteCarParksIfRemoved(hashMapRetrievedCarPark);
        } else {
            throw new ErrorServerException(ErrorCode.JSON_MAL_FORMATTED, "records can not be found in json");
        }
    }

    @Override
    public List<CarParkDto> provideListOrderedByNearest(GeoLocation comparedGeoLocation) throws JsonProcessingException {
        String url = getUrlCarParkInformation();
        String jsonCarParkInformation = webServiceCaller.get(url);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNodeCarParkLocation = objectMapper.readTree(jsonCarParkInformation);
        JsonNode jsonRecords = jsonNodeCarParkLocation.get("records");

        List<CarParkDto> carParkReturnedList = new ArrayList<>();

        if (jsonRecords != null && jsonRecords.isArray()) {
            int recordNum = 0;
            for (JsonNode jsonRecord : jsonRecords) {
                JsonNode jsonFields = getJsonNode(recordNum, jsonRecord, "fields");
                if (jsonFields == null) {
                    continue;
                }
                JsonNode jsonName = getJsonNode(recordNum, jsonFields, "nom");
                if (jsonName == null) {
                    continue;
                }
                JsonNode jsonCapacity = getJsonNode(recordNum, jsonFields, "capacite");
                if (jsonCapacity == null) {
                    continue;
                }
                JsonNode jsonNbFreeLocation = getJsonNode(recordNum, jsonFields, "places");
                if (jsonNbFreeLocation == null) {
                    continue;
                }
                String carParkName = jsonName.asText().toUpperCase();
                CarPark carPark = getHashMapExistingCarPark().get(carParkName);
                if (carPark == null) {
                    // The car park has not been referenced by loadCarParksInDB(), we can add it as we have its position
                    GeoLocation carParkGeoLocation = parseJsonGeoPoint2D(jsonFields, recordNum);
                    if (carParkGeoLocation == null) {
                        LOGGER.error("fields.geo_point_2d can not be loaded in record " + recordNum + " in url=" + url);
                        continue;
                    }
                    Town town = findTown();
                    carPark = new CarPark(null, town, carParkName, carParkGeoLocation.getLatitude(), carParkGeoLocation.getLongitude());
                }
                CarParkDto carParkDto = carParkToCarParkDtoMapper.map(carPark);
                carParkDto.setCapacity(jsonCapacity.asInt());
                carParkDto.setNbFreeLocations(jsonNbFreeLocation.asInt());
                validate(comparedGeoLocation);
                double distance = GeoUtil.calculateDistance(comparedGeoLocation, carParkDto.getGeoLocation()) * 1000D;
                carParkDto.setDistanceInMeters((int) distance);

                carParkReturnedList.add(carParkDto);

                recordNum++;
            }
        }
        return carParkReturnedList.stream()
                .sorted((cp1, cp2) -> cp1.getDistanceInMeters() - cp2.getDistanceInMeters())
                .toList();
    }

    private static GeoLocation parseJsonGeoPoint2D(JsonNode jsonFields, Integer recordNum) {
        JsonNode jsonGeoPoints = jsonFields.get("geo_point_2d");
        GeoLocation geoLocation = new GeoLocation();
        if (jsonGeoPoints != null && jsonGeoPoints.isArray()) {
            try {
                geoLocation.setLongitude(jsonGeoPoints.get(0).asDouble());
                geoLocation.setLatitude(jsonGeoPoints.get(1).asDouble());
            } catch (IndexOutOfBoundsException e) {
                LOGGER.error("geo_point_2d has not longitude and latitude in " + (recordNum == null ? "json" : ("jsonr record " + recordNum)));
                return null;
            }
        } else {
            return null;
        }
        return geoLocation;
    }
}