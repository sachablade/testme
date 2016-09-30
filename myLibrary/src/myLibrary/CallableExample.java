package myLibrary;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CallableExample {
	
	public static String[] TAGSEARCH = new String[] { "http://www.newpct1.com/series-hd/",
	"http://www.newpct1.com/serie/" };
	
	public static boolean contains(String line) {
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

	public static JSONObject retriveTask() throws Exception {		
		HashSet<String> set = new HashSet();
		Connection.Response form = Jsoup.connect("http://www.newpct1.com/pct1/library/include/ajax/get_subcategory.php")
	            .method(Connection.Method.POST)
	            .data("categoryIDR", "1469")
	            .execute();				
		Document doc = Jsoup.parse(new ByteArrayInputStream(form.bodyAsBytes()), "ISO-8859-1", "http://www.newpct1.com/pct1/library/include/ajax/get_subcategory.php");
		Elements options = doc.select("option");
		
		for (Element option : options) {
			Connection.Response formSearch = Jsoup.connect("http://www.newpct1.com/buscar")
		            .method(Connection.Method.POST)
		            .data("q", URLEncoder.encode(option.text(), "UTF-8"))
		            .execute();				
			doc = Jsoup.parse(new ByteArrayInputStream(formSearch.bodyAsBytes()), "ISO-8859-1", "http://www.newpct1.com/buscar");
			//Element box = doc.getElementsByClass("page-box").first();			
			Elements links = doc.select("a[href]");
			for (Element src : links) {
				String link=src.attr("abs:href");
				if(link.contains("serie")&&link.split("/").length>4 && contains(link)){
					set.add(src.attr("abs:href"));
					if(set.size()>15){
						JSONObject json = new JSONObject();
						JSONArray ja = new JSONArray();

						for (String s : set) {
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("key", s);
							ja.put(jsonObj);
						}
						json.put("input", ja);
						return json;
					}
					//System.out.println(src.attr("abs:href"));	
				}
				
			}
        }
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();

		for (String s : set) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("key", s);
			ja.put(jsonObj);
		}
		json.put("input", ja);
		return json;
	}

	  public static void main(String args[]) {
		JSONObject json = null;
		try {
			json = retriveTask();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    ExecutorService pool = Executors.newCachedThreadPool();
	    Set<Future<JSONObject>> set = new HashSet<Future<JSONObject>>();
	    JSONArray ja=(JSONArray) json.get("input");
	    for(int i=0;i<ja.length();i++){
	    	Callable<JSONObject> callable = new taskWorker1(ja.getJSONObject(i));
		    Future<JSONObject> future = pool.submit(callable);
		    set.add(future);
	    }
	    

	    
	    for (Future<JSONObject> future : set) {
	      try {
			System.out.println(future.get().get("key"));
		} catch (JSONException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }

	  }
	}