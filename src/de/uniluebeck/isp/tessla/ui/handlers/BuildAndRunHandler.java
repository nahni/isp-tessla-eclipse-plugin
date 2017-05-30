package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DefaultDockerClient.Builder;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.ContainerInfo;
import com.spotify.docker.client.messages.ExecCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import com.spotify.docker.client.messages.Volume;
import com.spotify.docker.client.messages.VolumeList;

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
		    hostPorts.add(PortBinding.of("127.0.0.1", port));
		    portBindings.put(port, hostPorts);
		}
		
		final HostConfig hostConfig = HostConfig.builder()
				.portBindings(portBindings)
//				.appendBinds("/local/path:/remote/path")
//				.appendBinds("C:/Users/lenovo/SSEProjekt/shared:c:/src")
//				.appendBinds("/Users/lenovo/SSEProjekt/shared:/src")
				.appendBinds("/home/annika/geteilt:/usr/geteilt")
				.build();

//		docker.pull("busybox");
		
//		-v /Users/<path>:/<container path> ...
//		https://docs.docker.com/engine/tutorials/dockervolumes/#mount-a-host-directory-as-a-data-volume
//		If you are using Docker Machine on Mac or Windows, your Docker Engine daemon has only limited access to your macOS or Windows filesystem. Docker Machine tries to auto-share your /Users (macOS) or C:\Users (Windows) directory. S
//		All other paths come from your virtual machine�s filesystem, so if you want to make some other host folder available for sharing, you need to do additional work.
//		String shared = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/shared:/src";
//		String shared = "C:/Users/lenovo/SSEProjekt/shared:c:/src";
//		String shared = "C:/Users/lenovo/SSEProjekt/shared:/src";
		String shared = "/Users/lenovo/SSEProjekt/shared:/src";
		
		// Create container with exposed ports
		final ContainerConfig containerConfig = ContainerConfig.builder()
		    .hostConfig(hostConfig)
//		    .image("busybox").exposedPorts(ports)
//		    .image("C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Atom/tessla").exposedPorts(ports)
		    .image("tessla").exposedPorts(ports)
		    //-rm
		    .cmd("sh", "-c", "while :; do sleep 1; done")
		    //-v : /src/webapp:/webapp This command mounts the host directory, /src/webapp, into the container at /webapp
//		    .cmd("sh", "-c", "while :; do sleep 1; done", "-v " + shared, "-w /src")
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
		
		final VolumeList volumeList = docker.listVolumes();
		final List<String> warnings = volumeList.warnings();
		final List<Volume> volumes = volumeList.volumes();
		
		if(warnings != null){
			System.out.println("Warnings:");
			for (String string : warnings) {
				System.out.println(string);
			}
		}
		
		if(volumes != null){
			System.out.println("Volumes: ");
			for (Volume volume : volumes) {
				System.out.println(volume);
			}
		}else{
			System.out.println("Volumes null!");
		}
		
		// Start container
		docker.startContainer(id);

		// Exec command inside running container with attached STDOUT and STDERR
//		final String[] command = {"bash", "-c", "ls"};
		String[] command = getArgs();
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

	private String[] getArgs() {
		// get docker command args
		// const outFile = path.join('build', this.activeProject.binName +
		// (buildAssembly ? '.bc' : ''))

		String activeProject_projPath = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/";
		String activeProject_binName = "sub_add_alternation";
		String outFile = "build/" + activeProject_binName + ".bc";
		boolean buildAssembly = true;

		List<String> args = new ArrayList<String>();
//		args.addAll(Arrays.asList("exec", "tessla", "clang", "-o", outFile));
		args.addAll(Arrays.asList("clang", "-o", outFile));

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

		//Stueck fuer Stueck:
//		 clang -S -emit-llvm foo.c
		args = new ArrayList<String>();
//		args.addAll(Arrays.asList("clang", "-S", "-emit-llvm", "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo.c"));

		//You're not in the same directory as your hello.c file. cd to the right directory, or use provide gcc with the path to the file:
		//gcc /home/oli/Desktop/hello.c -o hello
//		args.addAll(Arrays.asList("clang", "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo", "-o", "foo.c", "-S", "-emit-llvm"));

//		You need to have the code file (in this case helloworld.cpp) in your current working directory. If the code file is in a different directory, you need to specify where in the command. For example
//		g++ some/other/folder/helloworld.cpp -o helloworld.o
//		args.addAll(Arrays.asList("clang", "'C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo.c'", "-o", "foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "\"C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo.c\"", "-o", "foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "C:/Users/lenovo/Downloads/AIT/foo.c", "-o", "foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "C:/Users/lenovo/Downloads/AIT/foo.c", "-o", "C:/Users/lenovo/Downloads/AIT/foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "src/foo.c", "-o", "src/foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "/src/foo.c", "-o", "src/foo", "-S", "-emit-llvm"));
//		args.addAll(Arrays.asList("clang", "usr/src/foo.c", "-o", "src/foo", "-S", "-emit-llvm"));
		args.addAll(Arrays.asList("clang", "usr/geteilt/foo.c", "-o", "usr/geteilt/foo", "-S", "-emit-llvm"));

		
		
		//		clang -emit-llvm -o foo.bc -c foo.c
//		args.addAll(Arrays.asList("clang", "-emit-llvm", "-o", "foo.bc", "-c", "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo.c"));

//		clang -o foo foo.bc
//		args.addAll(Arrays.asList("clang", "-o", "-foo", "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/foo.c"));

		
		String[] argsArray = new String[args.size()];
		argsArray = args.toArray(argsArray);

		return argsArray;
	}
}
