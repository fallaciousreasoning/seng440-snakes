package nz.ac.canterbury.csse.a440.snakes.snake;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by wooll on 31-Jul-16.
 */
public class SnakeGPSController implements SnakeController, LocationListener {
    Direction direction = Direction.NORTH;
    String provider;
    private double prevousLong = 0;
    private double previousLat = 0;

    public SnakeGPSController(Location location) {
        if (location != null) {
            onLocationChanged(location);
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double currentLat = location.getLatitude();
        double currentLong = location.getLongitude();
        if (previousLat == 0 && prevousLong == 0) {
            previousLat = currentLat;
            prevousLong = currentLong;
            return;
        }

        double changeLat = previousLat - currentLat;
        double changeLong = prevousLong - currentLong;


        //TODO check validity of directions
        if (Math.abs(changeLat) > Math.abs(changeLong)) {
            if (changeLat > 0.00002) {
                direction = Direction.NORTH;
            } else {
                direction = Direction.SOUTH;
            }
        } else {
            if (changeLong > 0.00002) {
                direction = Direction.EAST;
            } else {
                direction = Direction.WEST;
            }
        }

        previousLat = currentLat;
        prevousLong = currentLong;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void reset() {
        direction = Direction.NORTH;
        previousLat = 0;
        prevousLong = 0;
    }
}
