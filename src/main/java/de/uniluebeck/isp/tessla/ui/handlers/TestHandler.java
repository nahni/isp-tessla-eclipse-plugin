package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.Activator;
import de.uniluebeck.isp.tessla.ui.TesslaPreferencePage;
import de.uniluebeck.isp.tessla.ui.services.CommandArgsService;
import de.uniluebeck.isp.tessla.ui.services.ConsoleService;
import de.uniluebeck.isp.tessla.ui.services.DockerService;
import de.uniluebeck.isp.tessla.ui.services.WorkingDirFileService;
import de.uniluebeck.isp.tessla.util.Constants;

public class TestHandler extends AbstractHandler {
	
	TeSSLaProject activeProject;
	
	private ConsoleService consoleService;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		this.consoleService = ConsoleService.getInstance();	
		
		printStd("Hello from TestHandler!");
		
		printStd(loadPluginSettings(TesslaPreferencePage.DOCKER_FILE_PREFERENCE));
		printStd(loadPluginSettings(TesslaPreferencePage.CONTAINER_DIR_PREFERENCE));
		printStd(loadPluginSettings(TesslaPreferencePage.PROJECT_PATH_PREFERENCE));
		
		return null;
	}
	
	private String loadPluginSettings(String key) {

		IEclipsePreferences prefs =
			    InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID); 
		return prefs.get(key, "def");
	}
	
	private void printStd(String text){
		consoleService.writeTo(Constants.STDOUT_CONSOLE_NAME, text);
	}
	
	private void run() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException{
		
		activeProject = new TeSSLaProject();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		
		String[] compileToLLVM_BC_Args = commandArgsService.getCompileToLLVM_BC_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToLLVM_BC_Args);
		
		System.out.println("compiled");
		
		String[] instrument_BC_Args = commandArgsService.getInstrument_BC_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(instrument_BC_Args);
		
		String[] compileToBinary_Args = commandArgsService.getCompileToBinary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToBinary_Args);
		
		String[] runBinary_Args = commandArgsService.getRunBinary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runBinary_Args);
		
		String[] addStandardLibrary_Args = commandArgsService.getAddStandardLibrary_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(addStandardLibrary_Args);

		//docker exec tessla sh -c cd /tessla && tessla spec2.tessla traces.log
		String[] runTeSSLa_Args = commandArgsService.getRunTeSSLa_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runTeSSLa_Args);
		
//		
//		dockerSerivce.stopContainer();
	}
	
	private void copyFiles(){
		activeProject = new TeSSLaProject();
		
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
	}
}
