package org.melato.android.ui;

public interface ListLoader {
  boolean isLoaded(int position);
  void load(int position);
}
