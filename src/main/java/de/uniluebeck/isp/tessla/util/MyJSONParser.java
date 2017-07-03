package de.uniluebeck.isp.tessla.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MyJSONParser {

	public static List<String> parse(File tsslJSONFile){
		List<String> outputArgs = new ArrayList<String>();
		
		 JSONParser parser = new JSONParser();
		    Object obj;
			try {
				obj = parser.parse(new FileReader(tsslJSONFile));
			    JSONObject jsonObject = (JSONObject) obj;
			    
			    JSONArray items = (JSONArray) jsonObject.get("items");
			    System.out.println(items);
			    
	            Iterator<JSONObject> iterator = items.iterator();
	            while (iterator.hasNext()) {
	            	JSONObject entry = iterator.next();
	                
	                if (entry.get("out") != null && entry.get("name") != null) {
	                    outputArgs.add("-o");
	                    outputArgs.add(entry.get("id") + ":" + entry.get("name"));
	                  }
	                
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
			return outputArgs;
	}
}
