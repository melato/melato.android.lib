package org.melato.android.util;

import org.melato.gps.Point2D;

public class LongitudeField extends LocationField {
  public LongitudeField(String label, Point2D point) {
    super(label, point);
  }

  @Override
  public String toString() {
    return label + ": " + point.getLon();
  }
}
