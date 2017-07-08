package de.uniluebeck.isp.tessla.util;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class PreferencesUtil {

	public static TeSSLaProject getTesslaProjectConfig(){
		IEclipsePreferences prefs =
			    InstanceScope.INSTANCE.getNode("de.uniluebeck.isp.Tessla.ui"); // does all the above behind the scenes

		String dockerFile= prefs.get("dockerFile", "def");
		String containerDir= prefs.get("containerDir", "def2");
		
		return new TeSSLaProject(containerDir, dockerFile);
	}
}
