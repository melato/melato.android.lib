/*-------------------------------------------------------------------------
 * Copyright (c) 2012,2013 Alex Athanasopoulos.  All Rights Reserved.
 * alex@melato.org
 *-------------------------------------------------------------------------
 * This file is part of Athens Next Bus
 *
 * Athens Next Bus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Athens Next Bus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Athens Next Bus.  If not, see <http://www.gnu.org/licenses/>.
 *-------------------------------------------------------------------------
 */
package org.melato.android.app;

import java.util.Map;

import org.melato.android.R;
import org.melato.client.HelpItem;
import org.melato.client.HelpStorage;
import org.melato.util.VariableSubstitution;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;

/** Displays interlinked help items that are taken from the database. */
public class HelpActivity extends Activity {
  public static final String KEY_NAME = "help_name";
  FrameworkApplication app;

  HelpItem getHelp() {
    if ( app == null )
      return null;
    HelpStorage db = app.getHelpStorage();
    String name = getIntent().getStringExtra(KEY_NAME);
    if (name != null) {
      String lang = getResources().getConfiguration().locale.getLanguage();
      return db.loadHelpByName(name, lang);
    }
    Uri uri = getIntent().getData();
    if (uri == null)
      return null;
    String path = uri.getSchemeSpecificPart();
    if (path != null) {
      return db.loadHelpByNode(path);
    }
    return null;
  }

  public static void showHelp(Context context, String name) {
    Intent intent = new Intent(context, HelpActivity.class);
    intent.putExtra(KEY_NAME, name);
    context.startActivity(intent);
  }

  protected void setHelpText(TextView view, HelpItem help) {
    if (help != null) {
      String text = help.getText();
      VariableSubstitution sub = new VariableSubstitution(
          VariableSubstitution.ANT_PATTERN);
      if ( app != null) {
        Map<String, String> vars = app.getApplicationVariables();
        text = sub.substitute(text, vars);
      }
      Spanned s = Html.fromHtml(text);
      view.setText(s);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Application app = getApplication();
    if ( app instanceof FrameworkApplication ) {
      this.app =(FrameworkApplication)app;
    }
    setContentView(R.layout.help);
    HelpItem help = getHelp();
    if (help != null) {
      setTitle(help.getTitle());
      TextView helpView = (TextView) findViewById(R.id.help);
      setHelpText(helpView, help);
      helpView.setMovementMethod(new LinkMovementMethod());
    }
  }

  static class HelpListener implements OnMenuItemClickListener {
    Context context;
    String helpId;

    public HelpListener(Context context, String helpId) {
      super();
      this.context = context;
      this.helpId = helpId;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
      showHelp(context, helpId);
      return true;
    }
  }

  public static void addItem(Menu menu, Context context, String helpId) {
    MenuItem item = menu.add(R.string.help);
    item.setOnMenuItemClickListener(new HelpListener(context, helpId));
    item.setIcon(R.drawable.help);
  }
}
