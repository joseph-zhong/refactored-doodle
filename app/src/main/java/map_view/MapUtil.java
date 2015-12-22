package com.instantly.app.map_view;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class MapUtil {
    /**
     * Converts Location to LagLng
     *
     * @param location
     * @return latlng
     */
    public static LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
