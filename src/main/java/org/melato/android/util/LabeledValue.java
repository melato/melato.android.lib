package org.melato.android.util;

import android.content.Context;

// Taken from nosmoke.android
/** An item is a label and a value. */
public class LabeledValue {
  int   id;
  String label;
  Object value;
  
  public LabeledValue(Context context, int labelResourceId, Object value ) {
    this.id = labelResourceId;
    this.label = context.getResources().getString(labelResourceId);
    this.value = value;
  }
  public LabeledValue(String label, Object value) {
    super();
    this.label = label;
    this.value = value;
  }
  @Override
  public String toString() {
    return formatProperty(label, value);
  }
  public int getId() {
    return id;
  }
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  public Object getValue() {
    return value;
  }
  public void setValue(Object value) {
    this.value = value;
  }        
  public static String formatProperty( String label, Object value ) {
    String s = (value == null) ? "" : value.toString();
    if ( label != null && label.length() != 0 ) {
      s = label + ": " + s;
    }
    return s;
  }
  
}

