package org.melato.android.app;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/** Displays a tooltip, using a short Toast. */
public class ToolTipListener implements View.OnLongClickListener {
  private int tooltipId;
  
  
  public ToolTipListener(int tooltipId) {
    super();
    this.tooltipId = tooltipId;
  }

  @Override
  public boolean onLongClick(View v) {
    if ( tooltipId != 0) {
      Context context = v.getContext();
      Toast.makeText(context, tooltipId, Toast.LENGTH_SHORT).show();
      return true;
    }
      return false;
  }
}

