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

import org.melato.client.Bookmark;

public class SqlBookmark extends Bookmark {
  private BookmarkDatabase db;
  private long id;

  SqlBookmark(BookmarkDatabase db, long id, int type, String name, Object object) {
    super(type, name, object);
    this.db = db;
    this.id = id;
  }
  @Override
  public Object getObject() {
    if ( object == null) {
      object = db.loadObject(id);
    }
    
    return super.getObject();
  }
  
  public void setName(String name) {
    this.name = name;
  }
  public long getId() {
    return id;
  }  
}
