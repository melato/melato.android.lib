package org.melato.android.location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.melato.gps.PointTime;
import org.melato.gpx.GPX;
import org.melato.gpx.GPXParser;
import org.melato.gpx.util.GPXIterators;
import org.melato.log.Log;
import org.melato.log.PLog;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

/** Simulate location updates.
 * This class has similar API to LocationManager, so it can be used as a one-line replacement for testing.
 * Replace:
 *     LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
 * With:
 *     Playback locationManager = Playback.getInstance();
 * @author Alex Athanasopoulos
 */
public class PlaybackManager {
  public static final String PROVIDER = "Playback";
  /** The name of the GPX file to play-back from, in the app files folder. */
  public static final String PLAYBACK_FILE = "playback.gpx";
  private static PlaybackManager instance;
  private List<LocationPlayback> clients = new ArrayList<LocationPlayback>();
  private Iterable<PointTime> waypoints;
  
  public static synchronized PlaybackManager getInstance(Context context) {
    if ( instance == null ) {
      instance = new PlaybackManager();
      instance.initialize(context);
    }
    return instance;
  }

  public PlaybackManager() {    
  }
  
  private void initialize(Context context) {
    File dir = context.getFilesDir();
    File file = new File(dir, PLAYBACK_FILE);
    initialize(file);
  }
  
  public void initialize(File gpxFile) {
    GPXParser gpxParser = new GPXParser();
    try {
      PLog.info( gpxFile );
      GPX gpx = gpxParser.parse(gpxFile);
      waypoints = GPXIterators.trackWaypoints(gpx);
      PLog.info( "loaded waypoints" );
    } catch (IOException e) {      
      throw new RuntimeException(e);
    }    
  }
  
  public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener) {
    Thread thread = Thread.currentThread();
    Log.info("requestLocationUpdates thread=" + thread.getClass().getName());
    LocationPlayback client = new LocationPlayback(listener);
    client.setMinTime(minTime);
    client.setMinDistance(minDistance);
    client.setWaypoints(waypoints.iterator()); // start from the beginning, for now.
    clients.add(client);
    new Thread(client).start();
  }

  public void removeUpdates(LocationListener listener) {
    int size = clients.size();
    for( int i = 0; i < size; i++ ) {
      LocationPlayback client = clients.get(i);
      if ( listener == client.listener) {
        client.stop();
        clients.remove(i);
        break;
      }
    }
  }
  
  public Location getLastKnownLocation(String provider) {
    if ( waypoints == null )
      return null;
    Iterator<PointTime> iterator = waypoints.iterator();
    if ( ! iterator.hasNext() ) {
      return null;
    }
    return LocationPlayback.newLocation(iterator.next(), PROVIDER, 0);
  }
}
