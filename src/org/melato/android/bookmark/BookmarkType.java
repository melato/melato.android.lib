package org.melato.android.bookmark;

import org.melato.client.Bookmark;

import android.content.Context;
import android.content.Intent;

public interface BookmarkType {
  int getIcon();
  Intent createIntent(Context context, Bookmark bookmark);
}
