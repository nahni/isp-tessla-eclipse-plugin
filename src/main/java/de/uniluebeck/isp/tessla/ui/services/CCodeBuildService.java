package de.uniluebeck.isp.tessla.ui.services;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class CCodeBuildService {

	TeSSLaProject activeProject;

	public CCodeBuildService(TeSSLaProject activeProject) {
		this.activeProject = activeProject;
	}

	public void buildCCode() {
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] compileToLLVM_BC_Args = commandArgsService.getCompileToLLVM_BC_Args();
		
		DockerService dockerSerivce = new DockerService(activeProject);
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(compileToLLVM_BC_Args);
	}
}
