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
package org.melato.android.bookmark;

import org.melato.android.db.Column;

import android.provider.BaseColumns;

/** Specifies the database schema, except for the statistics table. */
public final class BookmarkSchema {
  /** The version of the database schema */
  static final int DATABASE_VERSION = 1;

  public static final class Bookmarks extends BookmarkColumns implements
      android.provider.BaseColumns {
    /** The name of this table */
    public static final String TABLE = "bookmarks";

    public static final Column[] columns = {
      /** The timestamp of the record, in milliseconds. */
      new Column(TYPE, "INTEGER NOT NULL"),
      new Column(NAME, "TEXT NOT NULL"),
      new Column(OBJECT, "BLOB NOT NULL"),
      new Column(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT")};      
  }

  public static class BookmarkColumns {
    public static final String TYPE = "bookmark_type";
    public static final String NAME = "name";
    public static final String OBJECT = "obj";
  }

}
