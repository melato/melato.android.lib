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

import java.util.Collections;
import java.util.Map;

import org.melato.android.app.HomeActivity.InternalLaunchItem;
import org.melato.client.HelpStorage;
import org.melato.client.MenuStorage;
import org.melato.update.PortableUpdateManager;

import android.app.Activity;
import android.app.Application;


public abstract class AbstractFrameworkApplication extends Application implements FrameworkApplication {

  @Override
  public HelpStorage getHelpStorage() {
    return null;
  }

  @Override
  public MenuStorage getMenuStorage() {
    return null;
  }

  @Override
  public InternalLaunchItem[] getInternalLaunchItems() {
    return new InternalLaunchItem[0];
  }

  @Override
  public Map<String, String> getApplicationVariables() {
    return Collections.emptyMap();
  }

  @Override
  public int getEulaResourceId() {
    return 0;
  }

  @Override
  public Class<? extends Activity> getMainActivity() {
    return HomeActivity.class;
  }
  
}
