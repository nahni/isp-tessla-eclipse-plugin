package de.uniluebeck.isp.tessla.ui.services;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class TeSSLaService {

	TeSSLaProject activeProject;
	
	public TeSSLaService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
	}
	
	public void addStandardLibrary(){
//		getAddStandardLibrary_Args:  sh -c cat spec.tessla /usr/local/opt/tessla_rv/streams.tessla > spec2.tessla
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] addStandardLibrary_Args = commandArgsService.getAddStandardLibrary_Args();
		
		DockerService dockerSerivce = new DockerService();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(addStandardLibrary_Args);
	}
	
	public void runTeSSLa(){
//		getRunTeSSLa_Args:  sh -c cd /tessla && tessla spec2.tessla traces.log
		CommandArgsService commandArgsService = new CommandArgsService(activeProject);
		String[] runTeSSLa_Args = commandArgsService.getRunTeSSLa_Args();
		
		DockerService dockerSerivce = new DockerService();
		dockerSerivce.runDockerCommandAvoidingWordSplitting2(runTeSSLa_Args);
	}
}
