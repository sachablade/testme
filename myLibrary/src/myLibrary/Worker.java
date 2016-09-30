package myLibrary;

import java.util.concurrent.Callable;

import org.json.JSONObject;

public abstract class Worker implements Callable<JSONObject>{	
	JSONObject myInput;
	JSONObject myOutput;
	
	public abstract JSONObject runTask(JSONObject input) throws Exception;
	
	public Worker(JSONObject myInput) {
		super();
		this.myInput = myInput;
	}
	
	@Override
	public JSONObject call() throws Exception {
		// TODO Auto-generated method stub
		return myOutput;
	}

}
