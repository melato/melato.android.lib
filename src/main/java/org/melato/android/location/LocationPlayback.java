package org.melato.android.location;

import java.util.Iterator;

import org.melato.gps.Earth;
import org.melato.gps.PointTime;
import org.melato.log.Log;

import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;

public class LocationPlayback implements Runnable {
  private Iterator<PointTime> waypoints;
  LocationListener listener;
  private long minTime;
  private float minDistance;
  private String  provider = PlaybackManager.PROVIDER;
  /** The time to add to the waypoints before pushing them to the location listener */
  private long timeOffset;
  private boolean stop;
  /** The last point sent. */
  private PointTime last;
  /** The clock time when the last point was sent */
  private long      lastCurrentTimeMillis;
  private Thread thread;
  private Handler handler = new Handler();
  
  public LocationPlayback(LocationListener listener) {
    super();
    this.listener = listener;
  }

  public void setWaypoints(Iterator<PointTime> waypoints) {
    this.waypoints = waypoints;
  }

  public void setMinTime(long minTime) {
    this.minTime = minTime;
  }

  public void setMinDistance(float minDistance) {
    this.minDistance = minDistance;
  }

  public static Location newLocation(PointTime p, String provider, long timeOffset) {
    Location location = new Location(provider);
    location.setLatitude(p.getLat());
    location.setLongitude(p.getLon());
    location.setTime(p.getTime() + timeOffset);
    return location;
  }
  
  class LocationRunnable implements Runnable {
    Location location;
    
    public LocationRunnable(Location location) {
      super();
      this.location = location;
    }

    @Override
    public void run() {
      listener.onLocationChanged(location);      
    }
    
  }
  @Override
  public void run() {
    thread = Thread.currentThread();
    while( ! stop && waypoints.hasNext() ) {
      PointTime p = waypoints.next();
      Log.info( "next: " + p );
      if ( last != null ) {
        long simulatedElapsed = PointTime.timeDifferenceMillis(last,  p);
        if ( simulatedElapsed < minTime ) {
          continue;
        }
        if ( Earth.distance(last,  p) < minDistance) {
          continue;
        }
        long realElapsed = System.currentTimeMillis() - lastCurrentTimeMillis;
        long waitTime = simulatedElapsed - realElapsed;
        if ( waitTime > 0 ) {
          try {
            Log.info( "wait: " + waitTime );
            Thread.sleep(waitTime);
          } catch (InterruptedException e) {
            break;
          }
        }
      } else {
        timeOffset = System.currentTimeMillis() - p.getTime();
      }
      
      Log.info( "send: " + p );
      handler.post(new LocationRunnable(newLocation(p, provider, timeOffset)));
      last = p;
      lastCurrentTimeMillis = System.currentTimeMillis();
    }
  }
  
  public void stop() {
    stop = true;
    if ( thread != null ) {
      thread.interrupt();
    }    
  }

}
