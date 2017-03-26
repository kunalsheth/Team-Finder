package com.teamfinder.main.data;

/**
 * Created by the-magical-llamicorn on 3/25/17.
 */
public class Location {

    public final double latitude, longitude; // radians

    public static Location newLocationDegrees(final double latitude, final double longitude) {
        return new Location(Math.toRadians(latitude), Math.toRadians(longitude));
    }

    public static Location newLocationRadians(final double latitude, final double longitude) {
        return new Location(latitude, longitude);
    }

    protected Location(final double latitude, final double longitude) { // radians
        this.latitude = latitude; // radians
        this.longitude = longitude; // radians
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        else if (!(o instanceof Location)) return false;
        final Location that = (Location) o;
        return Double.compare(that.longitude, this.longitude) == 0
                && Double.compare(that.latitude, this.latitude) == 0;
    }

    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public double distance(final Location that) {
        final double deltaLatitude = (that.latitude - this.latitude);
        final double deltaLongitude = (that.longitude - this.longitude);
        final double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.cos((this.latitude)) * Math.cos((that.latitude))
                * Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

}
