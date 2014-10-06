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

import java.util.Map;

import org.melato.android.app.HomeActivity.InternalLaunchItem;
import org.melato.client.HelpStorage;
import org.melato.client.MenuStorage;
import org.melato.update.PortableUpdateManager;

/** Interface that provides application facilities that the framework uses.
 * It should be implemented by the android application's Application object.
 * It should return null or 0 for any options that it does not support.
 * @author alex
 *
 */
public interface FrameworkApplication {
  /** Return the help storage.  Used by the help activity to access help content.
   *  May be null.  */
  HelpStorage getHelpStorage();
  /** Return the menu storage, which provides configurable launch items.
   *  May be null. */
  MenuStorage getMenuStorage();
  /** Return the built-in menus */
  InternalLaunchItem[] getInternalLaunchItems();
  
  /** Return the application variables.  These are substituted in the help system.
   *  aa.  perhaps these could be incorporated in the HelpStorage.
   * */
  Map<String, String> getApplicationVariables();
  /** Return update manager, which provides application updates. */
  PortableUpdateManager getUpdateManager();
  /** Return the resource Id of the end user licence agreement, if any.
   * The EULA is shown to the user the first time the application is run.
   * @return A string resource id, or 0.
   */
  int getEulaResourceId();
}
