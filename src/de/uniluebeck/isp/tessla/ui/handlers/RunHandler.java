package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import de.uniluebeck.isp.tessla.ui.services.PatchedBinaryService;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RunHandler extends AbstractHandler {

//	private final static String PROJECT_PATH = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";
	
	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
		
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
		
//		Command command = event.getCommand();
//		command.getId();
//		
//		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Ui",
//				"Run");
		return null;
	}
	
	public void startDocker() throws DockerCertificateException, FileNotFoundException, IOException, DockerException, InterruptedException {
		Builder builder = DefaultDockerClient.fromEnv();
		builder.connectTimeoutMillis(60000);
		builder.readTimeoutMillis(60000);
		
		final DockerClient docker = builder.build();
		
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
//				.appendBinds("/local/path:/remote/path")
//				.appendBinds("C:/Users/lenovo/SSEProjekt/shared:c:/src")
//				.appendBinds("/Users/lenovo/SSEProjekt/shared:/src")
				.appendBinds("/home/annika/geteilt:/usr/geteilt")
				.build();

//		docker.pull("busybox");
		

		// Create container with exposed ports
		final ContainerConfig containerConfig = ContainerConfig.builder()
		    .hostConfig(hostConfig)
//		    .image("busybox").exposedPorts(ports)
		    .image("tessla").exposedPorts(ports)
		    //-rm
		    .cmd("sh", "-c", "while :; do sleep 1; done")
//		    .cmd(args)
		    .build();

		final ContainerCreation creation = docker.createContainer(containerConfig);
		final String id = creation.id();

		// Inspect container
		final ContainerInfo info = docker.inspectContainer(id);

		System.out.println("info: " + info);
		// geht in Linux iwie nicht, funktioniert nur auf Windows. Ka warum
//		Volume volumes = docker.inspectVolume(id);
//		System.out.println("volumes: " + volumes);
		
		// Start container
		docker.startContainer(id);

		// Exec command inside running container with attached STDOUT and STDERR
//		final String[] command = {"bash", "-c", "ls"};
		String[] command = getRunPatchedBinaryArgs();
		final ExecCreation execCreation = docker.execCreate(
		    id, command, DockerClient.ExecCreateParam.attachStdout(),
		    DockerClient.ExecCreateParam.attachStderr());
		
		
		final LogStream output = docker.execStart(execCreation.id());
		final String execOutput = output.readFully();

		System.out.println("execOutput: " + execOutput);
		
		// Kill container
		docker.killContainer(id);

		System.out.println("Container killed");
		
		// Remove container
		docker.removeContainer(id);

		// Close the docker client
		docker.close();

	}

	private String[] getRunPatchedBinaryArgs() {
		PatchedBinaryService patchedBinaryService = new PatchedBinaryService();
		
		return patchedBinaryService.getRunPatchedBinaryArgs();
	}
}
