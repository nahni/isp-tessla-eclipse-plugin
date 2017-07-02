package de.uniluebeck.isp.tessla.ui.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.ArrayUtils;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class DockerService2 {

	public void startDocker(TeSSLaProject activeProject) throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException{
		
		String host_dir = activeProject.getContainerDir();
		String tesslaFilePath = "/home/annika/Entwicklung/Files/tessla2-docker";
		
		String[] loadArgsArray = new String[] {"docker", "load", "-i",tesslaFilePath};
		runCommand(loadArgsArray);
		
		String[] argsArray = new String[] {"docker", "run", "-itd", "--volume", host_dir + ":/tessla", "-w", "/tessla", "--name", "tessla", "tessla", "sh"};
		runCommand(argsArray);
	}

	

	public void runDockerCommandAvoidingWordSplitting2(String[] command) {
		runCommand((String[]) ArrayUtils.addAll(new String[] {"docker", "exec", "tessla"}, command));
	}

	/**
	 * This avoids Words Splitting in console
	 * @param activeProject
	 */
	private void runCommand(String[] command) {
		try {
			
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(command);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));

			// read the output from the command
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);
			}

			// read any errors from the attempted command
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void stopContainer() throws DockerException, InterruptedException{
		// Kill container
		String[] argsArray = new String[] {"docker", "stop", "tessla"};
		runCommand(argsArray);
		
		// Remove container
		runCommand(new String[] {"docker", "rm", "tessla"});
	}

	
}
