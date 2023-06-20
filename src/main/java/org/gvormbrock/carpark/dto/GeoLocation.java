package org.gvormbrock.carpark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GeoLocation {
    private Double longitude;
    private Double latitude;
}
