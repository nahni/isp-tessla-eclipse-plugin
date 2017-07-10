package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.services.CommandArgsService;
import de.uniluebeck.isp.tessla.ui.services.ConsoleService;
import de.uniluebeck.isp.tessla.ui.services.DockerService;
import de.uniluebeck.isp.tessla.util.Constants;
import de.uniluebeck.isp.tessla.util.PreferencesUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BuildAndRunAndAnalyzeHandler extends AbstractHandler {

	TeSSLaProject activeProject;
	private ConsoleService consoleService;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.consoleService = ConsoleService.getInstance();
		
		printStd("Hello from BuildAndRunAndAnalyzeHandler");
		
		this.activeProject = new TeSSLaProject();
		
		try {
			printStd("Build, run and analyze project...");
			run();
		} catch (DockerCertificateException | IOException | DockerException | InterruptedException e) {
			printErr(e.getMessage());
		}

		return null;
	}
	
	private void run() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException{
		
		activeProject = PreferencesUtil.getTesslaProjectConfig();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.startDocker();
		
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		
		String[] compileToLLVM_BC_Args = commandArgsService.getCompileToLLVM_BC_Args();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToLLVM_BC_Args);
		
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
		
		//TODO
		dockerSerivce.stopContainer();
	}
	
	private void printStd(String text){
		consoleService.writeTo(Constants.STDOUT_CONSOLE_NAME, text);
	}
	
	private void printErr(String text){
		consoleService.writeTo(Constants.ERR_CONSOLE_NAME, text);
	}
}
