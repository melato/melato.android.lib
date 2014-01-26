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
package org.melato.android.ui;

import org.melato.android.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameFragment extends DialogFragment {
  private RenameHandler handler;
  private EditText textView;
  private int title = R.string.rename;
  private int ok = R.string.ok;
  
  public void setTitle(int title) {
    this.title = title;
  }
  public void setOk(int ok) {
    this.ok = ok;
  }
  public RenameFragment(RenameHandler handler) {
    super();
    this.handler = handler;
  }
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = inflater.inflate(R.layout.rename, null);
      builder.setView(view);
      textView = (EditText) view.findViewById(R.id.text);
      textView.setText(handler.getName());
      builder.setMessage(title);
      builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                   handler.setName(textView.getText().toString());
                 }
             });
      builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                 }
             });
      return builder.create();
  }
}
