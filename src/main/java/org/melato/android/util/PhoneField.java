package org.melato.android.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhoneField implements Invokable {
  String label;
  String text;
  String phone;
  
  public PhoneField(String text) {
    this(null, text);
  }
  public PhoneField(String label, String text) {
    this.label = label;
    this.text = text;
    this.phone = parsePhone(text);
  }
  
  public boolean isValid() {
    return phone.length() >= 10;
  }
  
  public static String parsePhone(String text) {
    StringBuilder buf = new StringBuilder();
    for( char c: text.toCharArray()) {
      if ( Character.isDigit(c)) {
        buf.append(c);
      } else if ( " -.()+".indexOf(c) >= 0 ) {
          // ignore marks that can separate digits
      } else {
        break;
      }
    }
    return buf.toString();
  }

  public String getText() {
    return text;
  }

  public String getPhone() {
    return phone;
  }

  @Override
  public String toString() {
    if ( label != null) {
      return label + ": " + text;
    } else {
      return text;
    }
  }
  @Override
  public void invoke(Context context) {
    Uri uri = Uri.fromParts( "tel", getPhone(), null );
    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
    context.startActivity(intent);       
  }
  
}
