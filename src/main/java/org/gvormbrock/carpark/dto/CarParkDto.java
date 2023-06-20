package org.gvormbrock.carpark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarParkDto {
    private String name;
    private GeoLocation geoLocation = new GeoLocation();
    private int capacity;
    private int nbFreeLocations;
    private int distanceInMeters;
}
