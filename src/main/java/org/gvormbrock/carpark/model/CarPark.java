package org.gvormbrock.carpark.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ListIndexBase;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarPark {

    // An autogenerated id (unique for each user in the db)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    Town town;

    @ListIndexBase
    @NotNull
    @Size(min = 2, max = 100, message = "Car park name must be minimum 2 characters and maximum 100 characters long")
    private String name;

    private  Double latitude;
    private  Double longitude;

    @Override
    public int hashCode() {
        return this.getName().hashCode() +
                this.getTown().hashCode() +
                this.getLongitude().hashCode() +
                this.getLatitude().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        CarPark carPark = (CarPark) obj;
        if (this.hashCode() != carPark.hashCode()) {
            return false;
        }
        return (this.getName().equals(carPark.getName()) &&
                this.getTown().getName().equals(carPark.getTown().getName()) &&
                this.getLongitude().equals(carPark.getLongitude()) &&
                this.getLatitude().equals(carPark.getLatitude()));
    }

}