package de.uniluebeck.isp.tessla.util;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.ui.services.WorkingDirFileService;

public class CopyFilesToContainer {
	
	public void copyFiles(TeSSLaProject activeProject){	
		//iwie sollte das wo anders hin oder die Methode umbeannt werden
		WorkingDirFileService workingDirFileService = new WorkingDirFileService(activeProject);
		workingDirFileService.createWorkingDir();
		workingDirFileService.transferFilesToContainer();
	}
	
}
