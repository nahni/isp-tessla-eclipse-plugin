package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;


import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.DefaultDockerClient.Builder;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BuildAndRunHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Ui",
//				"Build And Run");
		
		//Versuch 1
//		try {
////			Process p = Runtime.getRuntime().exec("docker images");
//			Process p = Runtime.getRuntime().exec("docker load -i C:/Users/lenovo/Downloads/Atom/tessla");
//			
//			InputStream errors = p.getErrorStream();
//			
//			System.out.println("fertig");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.out.println("Exc");
//		}
		
		try {
			startDocker();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DockerCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DockerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void startDocker() throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException {
		Builder builder = DefaultDockerClient.fromEnv();
		builder.connectTimeoutMillis(60000);
		builder.readTimeoutMillis(60000);
		
		final DockerClient docker = builder.build();
		
		final File imageFile = new File("C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Atom/tessla");
		final String image = "tessla" + System.nanoTime();
		try (InputStream imagePayload = new BufferedInputStream(new FileInputStream(imageFile))) {
		  docker.create(image, imagePayload);
		}
		
		final String[] ports = {"80"};
		final Map<String, List<PortBinding>> portBindings = new HashMap<>();
		for (String port : ports) {
		    List<PortBinding> hostPorts = new ArrayList<>();
		    
		    hostPorts.add(PortBinding.of("192.168.99.100", port));
		    portBindings.put(port, hostPorts);
		}
		
		final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

//		docker.pull("busybox");
		
		// Create container with exposed ports
		final ContainerConfig containerConfig = ContainerConfig.builder()
		    .hostConfig(hostConfig)
//		    .image("busybox").exposedPorts(ports)
//		    .image("C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Atom/tessla").exposedPorts(ports)
		    .image("tessla").exposedPorts(ports)
		    .cmd("sh", "-c", "while :; do sleep 1; done")
		    .build();

		final ContainerCreation creation = docker.createContainer(containerConfig);
		final String id = creation.id();

		// Inspect container
		final ContainerInfo info = docker.inspectContainer(id);

		System.out.println("info: " + info);
		
		// Start container
		docker.startContainer(id);

		// Exec command inside running container with attached STDOUT and STDERR
		final String[] command = {"bash", "-c", "ls"};
		final ExecCreation execCreation = docker.execCreate(
		    id, command, DockerClient.ExecCreateParam.attachStdout(),
		    DockerClient.ExecCreateParam.attachStderr());
		final LogStream output = docker.execStart(execCreation.id());
		final String execOutput = output.readFully();

		System.out.println("execOutput: " + execOutput);
		
		// Kill container
		docker.killContainer(id);

		// Remove container
		docker.removeContainer(id);

		// Close the docker client
		docker.close();

	}

	private String[] getArgs() {
		// get docker command args
		// const outFile = path.join('build', this.activeProject.binName +
		// (buildAssembly ? '.bc' : ''))

		String activeProject_projPath = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/";
		String activeProject_binName = "sub_add_alternation";
		String outFile = "build/" + activeProject_binName + ".bc";
		boolean buildAssembly = true;

		List<String> args = new ArrayList<String>();
		args.addAll(Arrays.asList("exec", "tessla", "clang", "-o", outFile));

		if (buildAssembly) {
			// args = args.concat(['-emit-llvm', '-S'])
			args.addAll(Arrays.asList("-emit-llvm", "-S"));
		}

		// put c files into args array
		// args = args.concat(this.activeProject.cFiles.map((arg) => {
		// return path.relative(this.activeProject.projPath, arg).replace(/\\/g,
		// '/')
		// }))

		args.add(activeProject_projPath + "foo.c");
		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
}
