package org.melato.android.bookmark;

import org.melato.client.Bookmark;

import android.app.Activity;
import android.content.Intent;

public interface BookmarkType {
  int getIcon();
  Intent createIntent(Activity activity, Bookmark bookmark);
}
