package org.gvormbrock.carpark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLocationDto {
    private String townName;
    private GeoLocation geoLocation;
}
