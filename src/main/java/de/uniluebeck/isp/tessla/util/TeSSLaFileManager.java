package de.uniluebeck.isp.tessla.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;


public class TeSSLaFileManager {

	public static void main(String[] args) {
		String sourceFile = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation/foo.c";
//		String sourceFile = "spec.tessla";
//		String sourceFile = ".gcc-flags.json";
//		String sourceFile = ".DS_Store";
		String projectPath = "C:/Annika/Studium/3 Semester/SSE Projekt/TeSSLa Plugin/Dateien zum ausprobieren/dummyProjectPath/sub_add_alternation";
		
		TeSSLaFileManager manager = new TeSSLaFileManager();
		try {
			List<String> names = manager.collectCFunctionsFromSourceFile(sourceFile, projectPath);
			for (String string : names) {
				System.out.println(string);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public List<String> collectCFunctionsFromSourceFile(String sourceFile, String projectPath) throws IOException {
	    // get function names
	    Map<String, FileFunctionInfo> namesAssoc = new HashMap<String, FileFunctionInfo>();
//	    List<FileFunctionInfo> names = new ArrayList<FileFunctionInfo>();
	    //vll doch eher so rum?
	    List<String> names = new ArrayList<String>();

	    // get file extension
	    String fileExtension = FilenameUtils.getExtension(sourceFile);	
        File file = new File(sourceFile);
        BufferedReader fileLines = new BufferedReader(new FileReader(file));
	    		
	    // if the source file is a C file
	    if (fileExtension.equals("c")) {
	      // create variables to get regex and matches
//	      String regex      = "/(?:[a-zA-Z][\\_\\w]*)(?:\\s*[\\*]?\\s+|\\s+[\\*]\\s*)([a-zA-Z][\\_\\w]*)\\s*\\(/g";
//	      String regex      = "/(?:[a-zA-Z][_\\w]*)(?:\\s*[*]?\\s+|\\s+[*]\\s*)([a-zA-Z][_\\w]*)\\s*\\(/g";
//	      						/(?:[a-zA-Z][_\w]*)(?:\s*[*]?\s+|\s+[*]\s*)([a-zA-Z][_\w]*)\s*\(/g
	      //mein gegoogelte Ausdruck
	      String regex      = "(?!\\b(if|while|for)\\b)\\b\\w+(?=\\s*\\()";
//	      var match      = ''
	      int lineCnt    = 1;

	      // loop over each line of code and get the match match
	     String line = "";
	     while ((line = fileLines.readLine()) != null) {
		        	
	    			Matcher matcher = Pattern.compile(regex).matcher(line);
	    			// do this as long as there are any matches
	    			while (matcher.find()){
	    				// get new match
	    				// match = regex.exec(line)
	    				// String match = matcher.group();
	    						
	    							// push match group 1 into names array
	    							String f = sourceFile.replace(projectPath + "/", "");
   									// var func = match[1]
	    							String func = matcher.group();
	    							
	    							//occurrence:   lineCnt + ':' + match.index
	    							//ist match.index aequivalent zu matcher.start()?
	    							String occurrence = lineCnt + ":" + matcher.start();
	    							namesAssoc.put(f + ":" + func, new FileFunctionInfo (f, func, occurrence));
	    			}

		        // increment line
		        lineCnt++;
	        } 	      

	     // copy elements back to names
	     Iterator<Entry<String, FileFunctionInfo>> iterator = namesAssoc.entrySet().iterator();
	      while(iterator.hasNext()){
	    	  Entry<String, FileFunctionInfo> entry = iterator.next();
//	    	  names.add(entry.getValue());
	    	  //vll doch eher so rum?
	    	  names.add(entry.getKey());
	      }
	      //ohje, ist das richtig da oben?
//	     // copy elements back to names
//	      for (var funcObj in namesAssoc) {
//	        names.push(namesAssoc[funcObj])
//	      }
	    } else if (fileExtension.equals("tessla")) {
	    	// if the file is a TeSSLa file	
	    	
	    	String line = "";
	    	while ((line = fileLines.readLine()) != null) {
	    		// first check if there is a function specified in this line
	    		int idx_fnCall = line.indexOf("function_calls(\"");
	    		int idx_comment = line.indexOf("--");
	    		
	    		// if there is a comment befor the function call skip this function call
	    		boolean onlyFunctionCall = idx_fnCall != -1 && idx_comment == -1;
	    		boolean functionCallBeforeComment = idx_fnCall != -1 && idx_comment != -1 && idx_fnCall < idx_comment;

		        if (onlyFunctionCall || functionCallBeforeComment) {
			          // extract function name
			          String functionName = line.substring(idx_fnCall + "function_calls(\"".length());
			          functionName = functionName.substring(0, functionName.indexOf("\""));

			          // das hab ich uebersprungen, wird doch eh dann in names transferiert
			          // namesAssoc[functionName] = functionName
			          names.add(functionName);
			        }
	    	}

	      // copy elements back to names
//	      for (var funcObj in namesAssoc) {
//	        names.push(namesAssoc[funcObj])
//	      }
	    }

	    // return function names as a array
	    return names;
	  }
	
    private class FileFunctionInfo {
		String fileName;
		String functionName;
		String occurrence;
        
		public FileFunctionInfo(String fileName, String functionName, String occurrence){
			this.fileName = fileName;
			this.functionName = functionName;
			this.occurrence = occurrence;
		}
        
    }

}
