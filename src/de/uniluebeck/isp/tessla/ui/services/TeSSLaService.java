package de.uniluebeck.isp.tessla.ui.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.uniluebeck.isp.tessla.model.TeSSLaProject;

public class TeSSLaService {

//	private final static String PROJECT_PATH = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
	private final static String PROJECT_PATH = "/home/annika/geteilt/dummyProjectPath/sub_add_alternation";
	private final static String OUTPUT_DIR = "";
	private final static String BIN_NAME = "";
	
	TeSSLaProject activeProject;
	
	public TeSSLaService(){
		activeProject = new TeSSLaProject(PROJECT_PATH, OUTPUT_DIR, BIN_NAME);
	}
	
	public String[] getRunTeSSLaArgs(){
		
		// skip if there is no active project!
		if(StringUtils.isBlank(activeProject.getProjectPath())){
//		      this.viewMgr.showNoProjectNotification()
		      return null;
		}

//		  // skip if there is no active project!
//	    if ( !this.activeProject.projPath ) {
//	      this.viewMgr.showNoProjectNotification()
//	      return
//	    }

		String containerDir = System.getProperty("user.home") + ".tessla-env";
//		this.containerDir   = path.join(os.homedir(), '.tessla-env')
		String containerBuild = containerDir + "/build";
//		this.containerBuild = path.join(this.containerDir, 'build')
		
		// get json file
		String tsslJSON = containerBuild + "/instrumented_" + activeProject.getBinName() + ".tessla.json";
	    // get json file
//	    var tsslJSON = path.join(this.containerBuild, 'instrumented_' + this.activeProject.binName + '.tessla.json')
	   File tsslJSONFile = new File(tsslJSON);
	   if(!tsslJSONFile.exists()){
//		      this.viewMgr.showNoTeSSLaJSONFoundNotification()
		   return null;	   
	   }
//		if ( !fs.existsSync(tsslJSON) ) {
//	      this.viewMgr.showNoTeSSLaJSONFoundNotification()
//	      return
//	    }

	   //brauch ich gar nicht
//	   String JSONString = FileUtils.readFileToString(tsslJSONFile);
	   
	    // get the first found file
//	    var JSONString      = fs.readFileSync(tsslJSON).toString()
//	    var tsslJSCONContent = JSON.parse(JSONString).items

	    List<String> outputArgs = new ArrayList<String>();
	    //Ich hab wieder den absoluten Pfad genommen und nicht den relativen
	    outputArgs.addAll(Arrays.asList("LANG=C.UTF-8", "/tessla_server" + tsslJSONFile.getAbsolutePath(), "--trace", "instrumented_" + activeProject.getBinName() + ".trace"));
	    		    
//	    var outputArgs = [
//	      'LANG=C.UTF-8',
//	      '/tessla_server',path.relative(this.containerDir, tsslJSON).replace(/\\/g, '/'),
//	      '--trace',
//	      'instrumented_' + this.activeProject.binName + '.trace'
//	    ]
	    
	    JSONParser parser = new JSONParser();
	    Object obj;
		try {
			obj = parser.parse(new FileReader(tsslJSONFile));
		    JSONObject jsonObject = (JSONObject) obj;
		    
		    Iterator it = jsonObject.entrySet().iterator();
	        while (it.hasNext()) {
	            Object entry = it.next();
	            System.out.println(entry);
	            //TODO ka was da rauskommt
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
//	    for (var id in tsslJSONContent) {
//	      var stream = tsslJSONContent[id]
//	      if (stream.out && stream.name) {
//	        outputArgs.push('-o')
//	        outputArgs.push(stream.id + ':' + stream.name)
//	      }
//	    }

        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("sh", "-c", "\\" + StringUtils.join(outputArgs, " ") + "\\"));
    	
	    // get args for the docker command
//	    const args = [
//	      'exec',
//	      'tessla',
//	      'sh',
//	      '-c',
//	      '\'' + outputArgs.join(' ') + '\''
//	    ]
		
	    
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getRunTeSSLaArgs: " + command);
        
        String[] argsArray = new String[args.size()];
        argsArray = args.toArray(argsArray);

        return argsArray;	    		
	}
	
	public String[] getBuildTeSSLaArgs(){
		
		// skip if there are no files to compile
		if(CollectionUtils.isEmpty(activeProject.getTeSSLaFiles())){
//		      this.viewMgr.showNoCompilableTeSSLaFilesNotification()
		      return null;
		}else if(activeProject.getTeSSLaFiles().size() > 1){
//			this.viewMgr.showTooMuchCompilableTeSSLaFilesNotification()
			//TODO: warum kein return null?
		}

//	    // skip if there are no files to compile
//	    if (!this.activeProject.tesslaFiles) {
//	      this.viewMgr.showNoCompilableTeSSLaFilesNotification()
//	      return
//	    } else if (this.activeProject.tesslaFiles.length > 1) {
//	      this.viewMgr.showTooMuchCompilableTeSSLaFilesNotification()
//	    }

		File file = activeProject.getTeSSLaFiles().get(0);
//	    const file = this.activeProject.tesslaFiles[0]


		//Ich hab den absoluten Pfad genommen
        List<String> args = new ArrayList<String>();
        args.addAll(Arrays.asList("java", "-jar", "/tessla-imdea-snapshot.jar", file.getAbsolutePath()));
    	
	    // create command and args
//	    const args = [
//	      'exec',
//	      'tessla',
//	      'java',
//	      '-jar',
//	      '/tessla-imdea-snapshot.jar',
//	      path.relative(this.activeProject.projPath, file).replace(/\\/g, '/')
//	    ]
        
        String command = "";
        for (String string : args) {
            command = command + " " + string;
        }
        System.out.println("getBuildTeSSLaArgs: " + command);
        
        String[] argsArray = new String[args.size()];
        argsArray = args.toArray(argsArray);

        return argsArray;
	}
	
}
