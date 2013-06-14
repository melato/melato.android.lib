package org.melato.android.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class UrlField implements Invokable {
  private String url;
  
  public UrlField(String url) {
    super();
    this.url = url;
  }
  
  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return url;
  }

  @Override
  public void invoke(Context context) {
    Uri uri = Uri.parse(getUrl());
    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    context.startActivity(intent);   
  }    
}
