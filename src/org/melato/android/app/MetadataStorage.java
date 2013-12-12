package org.melato.android.app;

import java.util.ArrayList;
import java.util.List;

import org.melato.client.HelpItem;
import org.melato.client.HelpStorage;
import org.melato.client.Menu;
import org.melato.client.MenuStorage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

/** Implements HelpStorage and MenuStoragem, using an SQLiteDatabase */
public class MetadataStorage implements HelpStorage, MenuStorage {
  private String databaseFile;
  
  public MetadataStorage(String databaseFile) {
    super();
    this.databaseFile = databaseFile;
  }

  protected SQLiteDatabase getDatabase() {    
    try {
      return SQLiteDatabase.openDatabase(databaseFile,
          null, SQLiteDatabase.OPEN_READONLY);
    } catch (SQLiteException e) {
      String message = e.getMessage();
      if ( message != null && message.contains("attempt to write a readonly database")) {
        // see http://metakinisi.melato.org/node/554
        SQLiteDatabase database = SQLiteDatabase.openDatabase(databaseFile, null, SQLiteDatabase.OPEN_READWRITE);
        database.close();
        return SQLiteDatabase.openDatabase(databaseFile,
            null, SQLiteDatabase.OPEN_READONLY);
      }
      throw e;
    }
  }


  protected String quote(String s) {
    if ( s.indexOf('\'') < 0 )
      return s;
    return s.replaceAll( "'", "''" );
  }
  
  private HelpItem loadHelpWhere(String where) {
    String sql = "select title, body, node, name from help where " + where;
    SQLiteDatabase db = getDatabase();
    try {
      Cursor cursor = db.rawQuery( sql, null);
      try {
        if ( cursor.moveToFirst() ) {
          int i = 0;
          HelpItem h = new HelpItem();
          h.setTitle(cursor.getString(i++));
          h.setText(cursor.getString(i++));
          h.setNode(cursor.getString(i++));
          if ( ! cursor.isNull(i)) {
            h.setName(cursor.getString(i));
          }
          return h;
        }
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
    return null;
  }
  
  @Override
  public HelpItem loadHelpByName(String name, String lang) {
    if ( lang == null) {
      return loadHelpWhere( "name = '" + quote(name) + "'");
    } else {
      String name2 = name + "." + lang;      
      return loadHelpWhere( "name IN ('" + quote(name) + "', '" + quote(name2) + "') ORDER BY name DESC");
    }
  }

  @Override
  public HelpItem loadHelpByNode(String node) {
    return loadHelpWhere( "node = '" + quote(node) + "'");
  }

  @Override
  public List<Menu> loadMenus() {
    String sql = "select label, type, target, icon, start_date, end_date from menus";
    List<Menu> menus = new ArrayList<Menu>();
    SQLiteDatabase db = getDatabase();
    try {
      Cursor cursor = db.rawQuery(sql, null);
      try {
        if ( cursor.moveToFirst() ) {
          do {
            Menu menu = new Menu();        
            int i = 0;
            menu.setLabel( cursor.getString(i++));
            menu.setType( cursor.getString(i++));
            menu.setTarget( cursor.getString(i++));
            if ( ! cursor.isNull(i)) {
              menu.setIcon(cursor.getString(i));
            }
            i++;
            menu.setStartDate(cursor.getInt(i++));
            menu.setEndDate(cursor.getInt(i++));
            menus.add(menu);
          } while( cursor.moveToNext() );
        }
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
    return menus;
  }

  @Override
  public byte[] loadImage(String name) {
    String sql = String.format("select image from images where name = '%s'", quote(name));
    SQLiteDatabase db = getDatabase();
    try {
      Cursor cursor = db.rawQuery(sql, null);
      try {
        if ( cursor.moveToFirst() ) {
          do {
            return cursor.getBlob(0);
          } while( cursor.moveToNext() );
        }
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
    return null;
  }

}
