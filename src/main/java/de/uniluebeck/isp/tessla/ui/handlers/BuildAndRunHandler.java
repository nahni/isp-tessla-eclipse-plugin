package de.uniluebeck.isp.tessla.ui.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;

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

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BuildAndRunHandler extends AbstractHandler {

	private final static String PROJECT_PATH = "/home/annika/Entwicklung/Spielwiese/dummyProjectPath3/sub_add_alternation";

	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	
	public BuildAndRunHandler(){
		activeProject = new TeSSLaProject();
	}
	
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

//	    // start compilation process
//	    this.onBuildCCode({                               // First compile C code into Assembly
//	      onSuccess: () => this.onPatchAssembly({         // then patch Assembly
//	        onSuccess: () => this.onBuildAssembly({       // compile patched Assembly
//	          onSuccess: () => this.onRunPatchedBinary({  // run patched binary
//	            onSuccess: () => this.onBuildTeSSLa({     // build TeSSLa code
//	              onSuccess: () => this.onRunTeSSLa({
//	                onSuccess: (lines) => {
//	                  //console.log(startTime)
//	                  // emit signal that components can update with correct output values
//	                  this.emitter.emit('format-tessla-output', {output: lines})
//	                }
//	              }),  // run TeSSLa server
//	              onError: this.viewMgr.highlightTeSSLaError
//	            })
//	          })
//	        })
//	      }),
//	      buildAssembly: true
//	    })		
		
		//TODO
		Main main = new Main();
		main.copyFiles();
//		main.run();
		
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
