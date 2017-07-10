package de.uniluebeck.isp.tessla.ui.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.ProcessOutput;
import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.Constants;

public class DockerService {

	final static Logger logger = Logger.getLogger(DockerService.class);
	
	TeSSLaProject activeProject;
	private ConsoleService consoleService;
	
	public DockerService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
		this.consoleService = ConsoleService.getInstance();
	}
	
	private void printDocker(String text){
		consoleService.writeTo(Constants.DOCKER_CONSOLE_NAME, text);
	}
	
	private void printErr(String text){
		consoleService.writeTo(Constants.ERR_CONSOLE_NAME, text);
	}
	
	public void startDocker() {
		
		String host_dir = activeProject.getContainerDir();
		String tesslaFilePath = activeProject.getDockerFile();
	
		String[] loadArgsArray = new String[] {"docker", "load", "-i",tesslaFilePath};
		runCommand(loadArgsArray);
		
		String[] argsArray = new String[] {"docker", "run", "-itd", "--volume", host_dir + ":/tessla", "-w", "/tessla", "--name", "tessla", "tessla", "sh"};
		runCommand(argsArray);
	}

	

	public void runDockerCommandAvoidingWordSplitting2(String[] command) {
		checkIfContainerIsRunning();
		runCommand((String[]) ArrayUtils.addAll(new String[] {"docker", "exec", "tessla"}, command));
	}

	/**
	 * This avoids Words Splitting in console
	 * @param activeProject
	 */
	private ProcessOutput runCommand(String[] command) {
		try {
			
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

			// read the output from the command
			String s1 = null;
			StringBuilder input = new StringBuilder();
			while ((s1 = stdInput.readLine()) != null) {
				input.append(s1+"\n");
			}
			
			s1 = null;
			StringBuilder error = new StringBuilder();
			// read any errors from the attempted command
			while ((s1 = stdError.readLine()) != null) {
			    error.append(s1+"\n");
			}
			
			if(StringUtils.isNotBlank(input.toString())){
				printDocker(input.toString());
			}
			
			if(StringUtils.isNotBlank(error.toString())){
				printErr(error.toString());
			}
			return new ProcessOutput(input.toString(), error.toString());
			
		} catch (IOException e) {
			logger.error("Docker command could not run successfully", e);
			printErr("DockerService: " + e.getMessage());
		}
		
		return null;
	}
	
	
	
	public void stopContainer() {
		// Kill container
		String[] argsArray = new String[] {"docker", "stop", "tessla"};
		runCommand(argsArray);
		
		// Remove container
		runCommand(new String[] {"docker", "rm", "tessla"});
	}
	
	public void checkIfContainerIsRunning() {
		//docker ps -q -f name=tessla
		ProcessOutput output = runCommand(new String[] {"docker", "ps", "-q", "-f", "name=tessla"});
		
		if(StringUtils.isBlank(output.getInfo())){
			startDocker();
		}
	}

	
}
