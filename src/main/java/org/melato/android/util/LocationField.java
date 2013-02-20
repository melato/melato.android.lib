package org.melato.android.util;

import org.melato.gps.Point2D;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

// Taken from nosmoke.android
public class LocationField implements Invokable {
  protected String label;
  protected Point2D point;
  
  public LocationField(String label, Point2D point) {
    super();
    this.label = label;
    this.point = point;
  }

  @Override
  public String toString() {
    return label + ": " + point.toString();
  }

  @Override
  public void invoke(Context context) {
    Uri uri = Uri.parse("geo:" + point.getLat() + "," + point.getLon());
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    context.startActivity(intent);
  }    
}
