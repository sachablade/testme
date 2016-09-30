package com.sachablade.example;

import java.util.ArrayList;

import org.json.JSONObject;

import com.sachablade.batch.WorkQueue;

public class Task2 extends WorkQueue {

	public static String[] TAGSEARCH = new String[] { "http://www.newpct1.com/series-hd/",
	"http://www.newpct1.com/serie/" };
	
	public Task2(String[] args) throws Exception {
		super(args,"");
	}

	public static void main(String[] args) throws Exception {	
		new Task2(args);
	}

	@Override
	public ArrayList<Object> retriveTask(JSONObject data) throws Exception {
		
		ArrayList<Object> returnList = new ArrayList<Object>();		
		returnList.add(1);
		returnList.add(2);
		returnList.add(3);
		returnList.add(4);
		returnList.add(5);
		returnList.add(6);
		returnList.add(7);
		returnList.add(8);
		returnList.add(9);
		returnList.add(10);
		
		return returnList;
	}

	@Override
	public Object runTask(Object Task) throws Exception {
		int task=(int) Task;
		int conversion= task*10;
	
		return conversion;
	}
	
	public boolean contains(String line) {
		if (TAGSEARCH == null) {
			return false;
		}
		for (int i = 0; i < TAGSEARCH.length; i++) {
			if (line.contains(TAGSEARCH[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void finalizeTask(Object Task) throws Exception {		
		int task=(int) Task;

		System.out.println(task);

	}
}