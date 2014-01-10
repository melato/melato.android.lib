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
