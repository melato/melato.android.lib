package org.melato.android.location;

import java.util.Date;

import org.melato.gps.Point;

import android.location.Location;

public class Locations {
  public static Point location2Point(Location loc) {
    if ( loc == null )
      return null;
    Point p = new Point( (float) loc.getLatitude(), (float) loc.getLongitude());
    p.setTime(new Date(loc.getTime()));
    if ( loc.hasSpeed() ) {
      p.setSpeed(loc.getSpeed());
    }
    if ( loc.hasAltitude() ) {
      p.setElevation((float)loc.getAltitude());
    }
    return p;
  }
}
