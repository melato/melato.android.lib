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
  /** Return the help storage.  Used by the help activity to access help content. */
  HelpStorage getHelpStorage();
  /** Return the menu storage, which provides configurable launch items. */
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
