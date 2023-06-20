package org.gvormbrock.carpark.util;

import org.gvormbrock.carpark.model.CarPark;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionsUtil {

    private CollectionsUtil() {}
    public static Set<Object> difference(Collection<?> carParks1, Collection<?> carParks2) {
        Set<Object> difference = new HashSet<>(carParks1);
        difference.removeAll(carParks2);
        return difference;
    }
}
