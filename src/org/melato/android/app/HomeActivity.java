/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013,2014, Alex Athanasopoulos.  All Rights Reserved.
 * alex@melato.org
 *-------------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *-------------------------------------------------------------------------
 */
package org.melato.android.app;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.melato.android.R;
import org.melato.android.util.Invokable;
import org.melato.client.Menu;
import org.melato.client.MenuStorage;
import org.melato.log.Log;
import org.melato.util.DateId;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/** Abstract Home Activity.
 * Implements the general functionality of the home screen, without knowing about specific menu items. 
 *  */
public class HomeActivity extends FrameworkActivity implements OnItemClickListener {
  List<LaunchItem> items = new ArrayList<LaunchItem>();
  public static interface LaunchItem extends Invokable {
    public void init(Button button);
  }
  public static abstract class InternalLaunchItem implements LaunchItem {
    int drawable;
    int text;
    int tooltip;
    
    public InternalLaunchItem(int drawable, int text, int tooltip) {
      super();
      this.drawable = drawable;
      this.text = text;
      this.tooltip = tooltip;
    }
    protected InternalLaunchItem(int drawable, int text) {
      this(drawable, text, text);
    }
    public void init(Button button) {
      button.setCompoundDrawablesWithIntrinsicBounds(0, drawable, 0, 0);
      button.setText(text);
      button.setOnLongClickListener(new ToolTipListener(tooltip));
      setButtonColors(button);
    }
  }
  public static class ActivityLaunchItem extends InternalLaunchItem {
    Class<? extends Activity> activity;
    public ActivityLaunchItem(Class<? extends Activity> activity, int drawable, int label, int tooltip) {
      super(drawable, label, tooltip);
      this.activity = activity;
    }        
    public ActivityLaunchItem(Class<? extends Activity> activity, int drawable, int label) {
      super(drawable, label);
      this.activity = activity;
    }        
    
    public void invoke(Context context) {
      context.startActivity(new Intent(context, activity));      
    }
  }
  public static class HelpLaunchItem extends InternalLaunchItem {
    private String helpName;
    
    public HelpLaunchItem(int icon, int label, String helpName) {
      super(icon, label);
      this.helpName = helpName;
    }

    public void invoke(Context context) {
      HelpActivity.showHelp(context, helpName);
    }
  }
  
  class MenuLaunchItem implements LaunchItem {
    Drawable drawable;
    Menu menu;
    
    public MenuLaunchItem(Context context, Menu menu, MenuStorage db) {      
      this.menu = menu;
      if ( menu.getIcon() != null) {
        byte[] icon = db.loadImage(menu.getIcon());
        if ( icon != null) { 
          Options options = new BitmapFactory.Options();
          options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
          InputStream in = new ByteArrayInputStream(icon);
          drawable = Drawable.createFromResourceStream(context.getResources(), null, in, menu.icon, options);
        }
      }
    }
    public void invoke(Context context) {
      if ( "url".equals( menu.type)){ 
        Uri uri = Uri.parse(menu.target);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
      } else if ("help".equals(menu.type)) {
        showHelp(context, menu.target);
      }
    }
    @Override
    public void init(Button button) {
      button.setText(menu.getLabel());
      button.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
      setButtonColors(button);
    }    
  }
  static void setButtonColors(Button button) {
    button.setBackgroundColor(Color.TRANSPARENT);
    button.setTextColor(Color.WHITE);
  }
  void initMenus() {
    FrameworkApplication app = getApp();
    if ( app != null) {
      for( LaunchItem item: app.getInternalLaunchItems() ) {
        items.add(item);
      }
      MenuStorage db = app.getMenuStorage();
      if ( db != null) {
        int dateId = DateId.dateId(new Date());
        for(Menu menu: db.loadMenus() ) {
          if ( menu.isActive(dateId)) {
            items.add( new MenuLaunchItem(this, menu, db));
          }
        }
      }
    }
  }
  public void showHelp(Context context, String target) {
    HelpActivity.showHelp(context, target);
  }
  /** Called when the activity is first created. */  
  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.info(getClass().getName() + " onCreate");
      if ( ! UpdateActivity.checkUpdates(this) ) {
        finish();
        return;
      }
      setContentView(R.layout.home);
      GridView grid = (GridView) findViewById(R.id.gridView);
      initMenus();
      grid.setAdapter(new ImageAdapter(this));
      grid.setOnItemClickListener(this);
  }


  void select(int position) {
    Invokable item = items.get(position);
    item.invoke(this);
  }
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position,
      long id) {
    select(position);
  }

  public class ImageAdapter extends BaseAdapter {
    private Context context;

    public ImageAdapter(Context c) {
        context = c;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            button = new Button(context);
            button.setLayoutParams(new GridView.LayoutParams(
                (int) getResources().getDimension(R.dimen.grid_width),                                                                                   
                (int) getResources().getDimension(R.dimen.grid_height)));
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }
        LaunchItem item = items.get(position);
        item.init(button);
        button.setOnClickListener(new ButtonListener(position));
        return button;
    }
  }
  
  class ButtonListener implements OnClickListener {
    int pos;
    public ButtonListener(int pos) {
      super();
      this.pos = pos;
    }

    @Override
    public void onClick(View v) {
      select(pos);
    }
    
  }
}
