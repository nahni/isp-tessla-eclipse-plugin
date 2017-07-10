package de.uniluebeck.isp.tessla.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.services.AssemblyService;
import de.uniluebeck.isp.tessla.ui.services.CCodeBuildService;
import de.uniluebeck.isp.tessla.ui.services.DockerService;
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
		onCompileAndRunProject();
		
		return null;
	}
	
	private void onCompileAndRunProject() {
		
		TeSSLaProject activeProject = PreferencesUtil.getTesslaProjectConfig();
		
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDirWithCopiedRessources();
		
		System.out.println("getContainerDir: " + activeProject.getContainerDir());
		System.out.println("getDockerFile: " + activeProject.getDockerFile());
		
		
		//TODO
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.startDocker();
		
		CCodeBuildService cCodeBuilder = new CCodeBuildService(activeProject);
		cCodeBuilder.buildCCode();
		
		AssemblyService assemblyService = new AssemblyService(activeProject);
		assemblyService.patchAssembly();
		assemblyService.buildAssembly();
		
		PatchedBinaryService patchedBinaryService = new PatchedBinaryService(activeProject);
		patchedBinaryService.runPatchedBinary();
		
		TeSSLaService teSSLaService = new TeSSLaService(activeProject);
		teSSLaService.addStandardLibrary();
		
		teSSLaService.runTeSSLa();
		
		//TODO
		dockerSerivce.stopContainer();
	}
	
}
