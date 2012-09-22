package org.melato.android.ui;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

/** An array adapter that checks if an item is available.
 * If not, it displays a temporary item and loads the item in the background.
 * Then it redraws.
 * @author Alex Athanasopoulos
 *
 * @param <T>
 */
public class BackgroundAdapter<T> extends ArrayAdapter<T> {
  private ListLoader loader;
  private Activity activity;
  private boolean isLoading;

  public BackgroundAdapter(Activity activity, ListLoader loader, int textViewResourceId,
      List<T> objects) {
    super(activity, textViewResourceId, objects);
    this.activity = activity;
    this.loader = loader;
  }
  
  class RefreshRunnable implements Runnable {
    @Override
    public void run() {
      Log.i("melato.org", "refresh");
      notifyDataSetChanged();      
    }    
  }

  class BackgroundLoad implements Runnable {
    int position;
    
    public BackgroundLoad(int position) {
      this.position = position;
    }

    @Override
    public void run() {
      loader.load(position);
      synchronized(BackgroundAdapter.this) {
        isLoading = false;
      }
      activity.runOnUiThread(new RefreshRunnable());
    }
    
  }
  @Override
  public T getItem(int position) {
    if ( ! loader.isLoaded(position)) {
      Log.i("melato.org", "not loaded: " + position );
      synchronized(this) {
        if ( ! isLoading ) {
          isLoading = true;
          new Thread(new BackgroundLoad(position)).start();
        }
      }
    }
    return super.getItem(position);
  }
}
