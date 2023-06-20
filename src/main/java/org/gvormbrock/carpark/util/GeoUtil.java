package org.gvormbrock.carpark.util;

import org.gvormbrock.carpark.dto.GeoLocation;

public class GeoUtil {
    /**
     * Haversine formula to calculate a distance between two GeoLocations
     *
     * @param location1 first location
     * @param location2 second location
     *
     * @return distance in km
     */
    public static double calculateDistance(GeoLocation location1, GeoLocation location2) {
        double earthRadius = 6371D; // In km

        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}
