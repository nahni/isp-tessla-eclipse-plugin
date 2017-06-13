package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;
import de.uniluebeck.isp.tessla.util.WorkingDirFileFilter;

public class WorkingDirFileService {

	private TeSSLaProject activeProject;
	
	public WorkingDirFileService(TeSSLaProject activeProject){
		this.activeProject = activeProject;
	}
	
	/**
	 * Erstelt das Verzeichnis auf dem Host-Computer, das in den Docker-Container gemappt wird auf /tessla
	 */
	public void createWorkingDir(){
		System.out.println("ContainerDir: " + activeProject.getContainerDir());
		Path path = Paths.get(activeProject.getContainerDir());
		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void transferFilesToContainer(){
		
		try {
			FileUtils.cleanDirectory(new File(activeProject.getContainerDir()));
			Files.createDirectories(Paths.get(activeProject.getContainerDir()+"/build"));
		
			createZlogFile();
			
			File src = new File(activeProject.getProjectPath());
			File dest = new File(activeProject.getContainerDir());
			FileUtils.copyDirectory(src, dest, new WorkingDirFileFilter());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createZlogFile(){
		//TODO
//	    // craft content of zlog file
//	    var binName   = this.activeProject.binName
//
//	    var formats = '[formats]\n'
//	    formats += 'variable_values = "' + atom.config.get('tessla.variableValueFormatting') + '"\n'
//	    formats += 'function_calls = "'  + atom.config.get('tessla.functionCallFormatting') + '"\n'
//
//	    var rules = '[rules]\n'
//	    rules += 'variable_values_cat.DEBUG "' + 'instrumented_' + binName + '.trace"; variable_values\n'
//	    rules += 'function_calls_cat.DEBUG "'  + 'instrumented_' + binName + '.trace"; function_calls\n'
//
//	    // first remove existing zlog
//	    if ( fs.existsSync(path.join(this.containerDir, 'zlog.conf')) ) {
//	      fs.unlinkSync(path.join(this.containerDir, 'zlog.conf'))
//	    }
//
//	    // then create new zlog.conf file
//	    fs.writeFileSync(path.join(this.containerDir, 'zlog.conf'), formats + rules)
	}
	
	
	
	
}
