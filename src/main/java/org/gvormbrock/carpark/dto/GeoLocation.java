package org.gvormbrock.carpark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class GeoLocation {
    @NonNull
    private Double longitude;
    @NonNull
    private Double latitude;
}
