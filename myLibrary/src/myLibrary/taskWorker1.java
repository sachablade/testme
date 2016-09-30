package myLibrary;

import org.json.JSONObject;

public class taskWorker1 extends Worker {

	public taskWorker1(JSONObject myInput) {
		super(myInput);
	}

	@Override
	public JSONObject runTask(JSONObject input) throws Exception {
		 try {
			 System.out.println(myInput.get("key"));
         } catch (Exception e) {
             System.out.println("Epic fail.");
         }
		
		return myInput;
	}

}
