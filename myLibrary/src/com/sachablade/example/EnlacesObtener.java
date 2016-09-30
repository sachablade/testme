package com.sachablade.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EnlacesObtener {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String[] TAGSEARCH = new String[] { "http://www.newpct1.com/series-hd/",
	"http://www.newpct1.com/serie/" };
	
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

	
	public void ObtenerEnlacesInit(String param1,Integer param2,Boolean param3) throws IOException{
		System.out.println(param1+param2+param3);
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
				if(link.contains("serie")&&link.split("/").length>4&&contains(link)){
					set.add(src.attr("abs:href"));
					System.out.println(src.attr("abs:href"));	
				}
				
			}
        }
		for (String s : set) {
		    System.out.println(s);
		}
		

	}
}
