package de.uniluebeck.isp.tessla.util;

import java.util.prefs.Preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.Activator;

public class PreferencesUtil {

	public static TeSSLaProject getTesslaProjectConfig(){
		IEclipsePreferences prefs =
			    InstanceScope.INSTANCE.getNode("de.uniluebeck.isp.Tessla.ui"); // does all the above behind the scenes

		String dockerFile= prefs.get("dockerFile", "def");
		String containerDir= prefs.get("containerDir", "def2");
		
		return new TeSSLaProject(containerDir, dockerFile);
	}
	
	public static void probe2(){
		IProject project = null;

		IScopeContext[] contexts;
		if (project != null) {
		    contexts = new IScopeContext[] { new ProjectScope(project),
		            InstanceScope.INSTANCE, ConfigurationScope.INSTANCE,
		            DefaultScope.INSTANCE };
		} else {
		    contexts = new IScopeContext[] { InstanceScope.INSTANCE,
		            ConfigurationScope.INSTANCE, DefaultScope.INSTANCE };
		}

		String value = null;

		for (int i = 0; i < contexts.length; ++i) {
		    value = contexts[i].getNode(Activator.PLUGIN_ID).get("dockerFile", null);

		    if (value != null) {
		        value = value.trim();

		        if (!value.isEmpty()) {
		            break;
		        }
		    }
		}

//		if (value != null) {
//		    return value;
//		}
		System.out.println("probe2: " + value);
	}
	
	public static void probe3(){
		IEclipsePreferences preferences = DefaultScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		String value = preferences.get("dockerFile", null);
		
		System.out.println("probe3: " + value);
		System.out.println("probe3: " + preferences.get("launchCount", null));
	}
}
