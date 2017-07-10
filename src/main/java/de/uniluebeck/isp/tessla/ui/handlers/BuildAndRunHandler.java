package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.services.AssemblyService;
import de.uniluebeck.isp.tessla.ui.services.CCodeBuildService;
import de.uniluebeck.isp.tessla.ui.services.DockerService;
import de.uniluebeck.isp.tessla.ui.services.Main;
import de.uniluebeck.isp.tessla.ui.services.PatchedBinaryService;
import de.uniluebeck.isp.tessla.ui.services.TeSSLaService;
import de.uniluebeck.isp.tessla.ui.services.WorkingDirFileService;
import de.uniluebeck.isp.tessla.util.PreferencesUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BuildAndRunHandler extends AbstractHandler {
	

	
	public BuildAndRunHandler(){
		
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		System.out.println("BuildAndRunHandler");
		try {
			onCompileAndRunProject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch 1");
		} catch (DockerCertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch 2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch 3");
		} catch (DockerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch 4");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Catch 5");
		}
		
		return null;
	}
	
	private void onCompileAndRunProject() throws FileNotFoundException, DockerCertificateException, IOException, DockerException, InterruptedException {
		
		TeSSLaProject activeProject = PreferencesUtil.getTesslaProjectConfig();
		
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDirWithCopiedRessources();
		
		System.out.println("getContainerDir: " + activeProject.getContainerDir());
		System.out.println("getDockerFile: " + activeProject.getDockerFile());
		
		
		//TODO
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.startDocker();
		
		System.out.println("onBuildCCode");
		CCodeBuildService cCodeBuilder = new CCodeBuildService(activeProject);
		cCodeBuilder.buildCCode();
		
		System.out.println("onPatchAssembly");
		AssemblyService assemblyService = new AssemblyService(activeProject);
		assemblyService.patchAssembly();
		
		System.out.println("onBuildAssembly");
		assemblyService.buildAssembly();
		
		System.out.println("RunPatchedBinary");
		PatchedBinaryService patchedBinaryService = new PatchedBinaryService(activeProject);
		patchedBinaryService.runPatchedBinary();
		
		System.out.println("BuildTeSSLa");
		TeSSLaService teSSLaService = new TeSSLaService(activeProject);
		teSSLaService.addStandardLibrary();
		
		System.out.println("RunTeSSLa");
		teSSLaService.runTeSSLa();
		
		//TODO
		dockerSerivce.stopContainer();
	}
	
}
