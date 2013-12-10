package org.melato.android.app;

import java.util.Map;

import org.melato.client.HelpStorage;

public interface FrameworkApplication {
  HelpStorage getHelpStorage();
  Map<String, String> getApplicationVariables();
}
