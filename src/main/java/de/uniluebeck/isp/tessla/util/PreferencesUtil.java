package de.uniluebeck.isp.tessla.util;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.Activator;
import de.uniluebeck.isp.tessla.ui.TesslaPreferencePage;

public class PreferencesUtil {

	public static TeSSLaProject getTesslaProjectConfig(){
		IEclipsePreferences prefs =
			    InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);

		String dockerFile= prefs.get(TesslaPreferencePage.DOCKER_FILE_PREFERENCE, "def");
		String containerDir= prefs.get(TesslaPreferencePage.CONTAINER_DIR_PREFERENCE, "def2");
		
		if(StringUtils.isBlank(containerDir)){
			containerDir = System.getProperty("user.home") + "/" + ".tessla-env";
		}
		
		return new TeSSLaProject(containerDir, dockerFile);
		
	}
}
