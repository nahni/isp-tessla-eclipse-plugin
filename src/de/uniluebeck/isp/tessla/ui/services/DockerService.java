package de.uniluebeck.isp.tessla.ui.services;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.DefaultDockerClient.Builder;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class DockerService {

	private DockerClient docker;
	
	private String containerId = "";
	
	public void startDocker(TeSSLaProject activeProject) throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException{
		//docker run --volume /home/annika/Entwicklung/Spielwiese/dummyProjectPath:/tessla -w /tessla -tid --name tessla tessla sh
		
		String host_dir = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath";
		
		List<String> args = new ArrayList<String>();
		args.addAll(Arrays.asList("docker", "run", "--volume", host_dir + ":/tessla", "-w", "/tessla", "-tid", "--name", "tessla", "tessla", "sh"));
	
		
		// -------------------------------------
		
		Builder builder = DefaultDockerClient.fromEnv();
		builder.connectTimeoutMillis(60000);
		builder.readTimeoutMillis(60000);
		
		docker = builder.build();
		
//		final File imageFile = new File("C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Atom/tessla");
		final File imageFile = new File("/home/annika/Entwicklung/Files/tessla");
		final String image = "tessla" + System.nanoTime();
		try (InputStream imagePayload = new BufferedInputStream(new FileInputStream(imageFile))) {
		  docker.create(image, imagePayload);
		}
		
		final String[] ports = {"80"};
		final Map<String, List<PortBinding>> portBindings = new HashMap<>();
		for (String port : ports) {
		    List<PortBinding> hostPorts = new ArrayList<>();
		    
		    // die 192.168.99.100 geht bei Linux iwie nicht. Funktioniert nur auf Windows
//		    hostPorts.add(PortBinding.of("192.168.99.100", port));
		    //und das geht bei mir nur auf Windows aber nicht auf Linux
		    hostPorts.add(PortBinding.of("127.0.0.1", port));
		    portBindings.put(port, hostPorts);
		}
		
//		-v /Users/<path>:/<container path> ...
//		https://docs.docker.com/engine/tutorials/dockervolumes/#mount-a-host-directory-as-a-data-volume
//		If you are using Docker Machine on Mac or Windows, your Docker Engine daemon has only limited access to your macOS or Windows filesystem. Docker Machine tries to auto-share your /Users (macOS) or C:\Users (Windows) directory. S
//		All other paths come from your virtual machine�s filesystem, so if you want to make some other host folder available for sharing, you need to do additional work.
		final HostConfig hostConfig = HostConfig.builder()
				.portBindings(portBindings)
//				.appendBinds("/home/annika/geteilt:/usr/geteilt")
				.appendBinds(activeProject.getContainerDir() + ":/tessla")
				.build();

//		docker.pull("busybox");
		
		final ContainerConfig containerConfig = ContainerConfig.builder()
			    .hostConfig(hostConfig)
			    .image("tessla").exposedPorts(ports)
			    //-rm
//			    .cmd("--volume", host_dir + ":/tessla", "-w", "/tessla", "-tid", "--name", "tessla", "tessla", "sh")
//			    .cmd("-w", "/tessla", "-tid", "--name", "tessla", "tessla", "sh")
//			    .cmd("-tid", "--name", "tessla", "tessla", "sh")
//			    .cmd("-tid", "tessla", "sh")
			    .cmd("sh", "-c", "while :; do sleep 1; done")
			    //wenn man die working dir setzt gehts iwie nicht mehr
			    .workingDir("/tessla")
//			    .cmd(args)
			    .build();
	
		

		
//		final ContainerCreation creation = docker.createContainer(containerConfig);
		final ContainerCreation creation = docker.createContainer(containerConfig, "tessla");
		final String id = creation.id();
		this.containerId = id;
		
		// Inspect container
		final ContainerInfo info = docker.inspectContainer(id);

		System.out.println("info: " + info);
		// geht in Linux iwie nicht, funktioniert nur auf Windows. Ka warum
//		Volume volumes = docker.inspectVolume(id);
//		System.out.println("volumes: " + volumes);
		
		// Start container
		docker.startContainer(id);
	
	
	}
	
	public void runDockerCommand(String[] command) throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException {
		runDockerCommand(command, false);
	}
	
	public void runDockerCommand(String[] command, boolean logOn) throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException {


		// Exec command inside running container with attached STDOUT and STDERR
//		final String[] command = {"bash", "-c", "ls"};
//		String[] command = getBuildAssemblyArgs();
		final ExecCreation execCreation = docker.execCreate(
			containerId, command, DockerClient.ExecCreateParam.attachStdout(),
		    DockerClient.ExecCreateParam.attachStderr());
		
		
		final LogStream output = docker.execStart(execCreation.id());
		
		if(logOn){
			final String execOutput = output.readFully();
			System.out.println("execOutput: " + execOutput);
		}

		
//		// Kill container
//		docker.killContainer(id);
//		
//		// Remove container
//		docker.removeContainer(id);
//
//		// Close the docker client
//		docker.close();
	}
	
	public void runDockerCommand2(String command){
		try {
			Runtime rt = Runtime.getRuntime();
//			Process proc = rt.exec("gksudo docker exec tessla " + command);
			Process proc = rt.exec("docker exec tessla " + command);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));

//			// read the output from the command
//			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);
			}

			// read any errors from the attempted command
//			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runDockerCommand3(String command){
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("gksudo docker exec tessla " + command);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));

//			// read the output from the command
//			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);
			}

			// read any errors from the attempted command
//			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * schreibt noch das tessla.json raus
	 * @param command
	 */
	public void runCommandForBuildTeSSLa(String command, TeSSLaProject activeProject){
		try {
			Runtime rt = Runtime.getRuntime();
//			Process proc = rt.exec("gksudo docker exec tessla " + command);
			Process proc = rt.exec("docker exec tessla " + command);
			
			BufferedReader stdInput = new BufferedReader(new 
			     InputStreamReader(proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
			     InputStreamReader(proc.getErrorStream()));

//			// read the output from the command
//			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			StringBuilder sb = new StringBuilder();
			while ((s = stdInput.readLine()) != null) {
			    System.out.println(s);;
			    sb.append(s);
			    sb.append("\n");
			}
			createTeSSLaJson(sb.toString(), activeProject);
			// read any errors from the attempted command
//			System.out.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
			    System.out.println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createTeSSLaJson(String stdout, TeSSLaProject activeProject){
//	     // check for compiler errors
//	      if (stdout.charAt(0) == '{') {
//	        // cut trailing comma
//	        //
//	        // Why the hell the compiler puts an trailing ',' to the string?? should
//	        // actually be fixed in the compiler binary not in this package!!!!
//	        if (stdout.charAt(stdout.length - 2) == ',') {
//	          stdout = stdout.slice(0, -2) + '\n'
//	        }
//
//	        // here we know the compilation process was successful so write content to file
//	        fs.writeFileSync(path.join(this.containerBuild, 'instrumented_' + this.activeProject.binName + '.tessla.json'), stdout)
//	      }
		
//	     // check for compiler errors
	      if (stdout.charAt(0) == '{') {
		        // Why the hell the compiler puts an trailing ',' to the string?? should
		        // actually be fixed in the compiler binary not in this package!!!!
		        if (stdout.charAt(stdout.length() - 2) == ',') {
		          stdout = stdout.substring(0, -2) + "\n";
		        }
		        // here we know the compilation process was successful so write content to file
				try {
					FileUtils.writeStringToFile(new File(activeProject.getContainerDir() + "/build/" + "instrumented_" + activeProject.getBinName() + ".tessla.json"), stdout);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	      }else{
	    	  System.out.println("Error");
	      }
	}
	
	public void removeContainer() throws DockerException, InterruptedException{
		
		Thread.sleep(3000);
		
		// Kill container
		docker.killContainer(containerId);
		
		// Remove container
		docker.removeContainer(containerId);

		// Close the docker client
		docker.close();
	}
	
}
