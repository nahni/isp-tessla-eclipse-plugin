package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;

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
		
		DockerClientConfig  config  = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
		
//		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//				  .withDockerHost("tcp://docker.somewhere.tld:2376")
//				  .withDockerTlsVerify(true)
//				  .withDockerCertPath("/home/user/.docker")
//				  .withRegistryUsername(registryUser)
//				  .withRegistryPassword(registryPass)
//				  .withRegistryEmail(registryMail)
//				  .withRegistryUrl(registryUrl)
//				  .build();
		
		// using jaxrs/jersey implementation here (netty impl is also available)
		DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
		  .withReadTimeout(1000)
		  .withConnectTimeout(1000)
		  .withMaxTotalConnections(100)
		  .withMaxPerRouteConnections(10);

		DockerClient dockerClient = DockerClientBuilder.getInstance(config)
		  .withDockerCmdExecFactory(dockerCmdExecFactory)
		  .build();
		
		Info info = dockerClient.infoCmd().exec();
		System.out.println("Info");
		System.out.print(info);
		
		
//		Create new Docker container, start and stop it:
		//geht iwie nicht
//		CreateContainerResponse container = dockerClient.createContainerCmd("file://home/annika/Entwicklung/Files/tessla")
//				.withCmd("touch", "/test")
//				.exec();
		
//		CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
//		.withCmd("touch", "/test")
//		.exec();

		//Alternative
//		Path path = Paths.get(URI.create("file://home/annika/Entwicklung/Files/tessla"));
//		Path path = Paths.get("file://home/annika/Entwicklung/Files/tessla");
//		Path path = Paths.get("/home/annika/Entwicklung/Files/tessla");
		Path path = Paths.get("home/annika/Entwicklung/Files/tessla");
//		Path path = Paths.get(URI.create("/home/annika/Entwicklung/Files/tessla"));
		//		dockerClient.execCreateCmd(arg0)
		try {
			InputStream is = Files.newInputStream(path, null);
			dockerClient.loadImageCmd(is).exec();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		dockerClient.startContainerCmd(container.getId()).exec();
//		dockerClient.stopContainerCmd(container.getId()).exec();
//		dockerClient.waitContainerCmd(container.getId()).exec();

		System.out.println("fertig");
		
		return null;
	}
}
