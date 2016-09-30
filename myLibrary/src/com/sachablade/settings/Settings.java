package com.sachablade.settings;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sachablade.batch.TaskObjectExecutor;

public class Settings {	
	
	private JSONObject settings;	
	private List<JSONObject> tasks;
	private List<TaskObjectExecutor> ObjectExecutorList;
	
	public JSONObject getSettings() {
		return settings;
	}	
	public ArrayList<TaskObjectExecutor> getTask(){
		return (ArrayList<TaskObjectExecutor>) ObjectExecutorList;		
	}
	public TaskObjectExecutor getTask(int index){
		return ObjectExecutorList.get(index);		
	}
	
	public Settings(String settingsPath) throws ClassNotFoundException, NoSuchMethodException, IOException {
		tasks = new ArrayList<JSONObject>();
		ObjectExecutorList= new ArrayList<TaskObjectExecutor>();		
		this.settings = getSettings(settingsPath);	
		getTasks();
	}

	private  JSONObject getSettings(String settingsPath) throws IOException {		
		String path = settingsPath;
		String content = readFile(path, StandardCharsets.UTF_8);
		JSONObject jObject  = new JSONObject(content); 
		return jObject;
	}

	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	private void getTasks() throws ClassNotFoundException, NoSuchMethodException{
		JSONArray ja = settings.getJSONArray("Tasks");
		for(Object json:ja){
			JSONObject jsonItem = (JSONObject) json;
			tasks.add(jsonItem);			
			
			String className = jsonItem.getString("ClassName");
			try {
				Object xyz = Class.forName(className).newInstance();
				String method=jsonItem.getJSONObject("method").getString("name");
				Object[] parameters=null;
				try{
					JSONArray arrJsonParameters= jsonItem.getJSONObject("method").getJSONArray("parameters");	
					parameters=new Object[arrJsonParameters.length()];
					for(int i=0;i<arrJsonParameters.length();i++)					
						parameters[i]=arrJsonParameters.get(i);
					System.out.println(parameters);
				}catch(JSONException e){
					
				}
				
					
				
				TaskObjectExecutor refTaskObjectExecutor=new TaskObjectExecutor(xyz, method ,parameters);
				ObjectExecutorList.add(refTaskObjectExecutor);
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}	
		System.out.println(tasks.get(0));
	}
	
	
	

}
