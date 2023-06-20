package org.gvormbrock.carpark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InputDto {
    private int nbFreeCarPark;
    private String countryCode;
    private String townName;
    private GeoLocation geoLocation = new GeoLocation();
}
