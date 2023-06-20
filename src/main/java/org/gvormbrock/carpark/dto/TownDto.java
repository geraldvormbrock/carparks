package org.gvormbrock.carpark.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TownDto {

    private Long id;

    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s\\-()'.]*$", message = "Country name can be only be alphanumerical characters")
    @Size(min = 3, max = 100, message = "Town name name must be minimum 3 characters and maximum 100 characters long")
    private String name;

    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s\\-()'.]*$", message = "Country name can be only be alphanumerical characters")
    @Size(min = 3, max = 100, message = "Country name must be minimum 3 characters and maximum 100 characters long")
    private String countryCode;
}
