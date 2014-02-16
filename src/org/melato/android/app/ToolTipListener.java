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

