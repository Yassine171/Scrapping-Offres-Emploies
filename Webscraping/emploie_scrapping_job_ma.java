	package Webscraping;
	import java.io.FileWriter;
	import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
	import org.jsoup.Jsoup;  
	import org.jsoup.nodes.Document;
	import org.jsoup.nodes.Element;
	import org.jsoup.select.Elements;
	import org.json.JSONArray;
	import org.json.JSONObject;
	
public class emploie_scrapping_job_ma {


		public static String time_format(long sec) {
			long r = sec;
			
		    long days    = (long) r / (24*60*60);
		    r %= (24*60*60);
		    
		    long hours   = (long) r / (60*60);
		    r %= (60*60);
		    
		    long minutes = (long) r / 60;
		    long seconds = (long) r % 60;
		    
		    return days+"d - " + hours + "h - " + minutes+"m - " + seconds + "s";
		}
		
		public static void show(JSONArray l) {
			int i=0;
		    while(i < l.length()) {
		    	show(l.getJSONObject(i));
		    	i++;
		    }
		}
		
		public static void show(JSONObject l) {
		   for(Object name: l.names()) {
			   System.out.println("'" + name + "' : '" + l.get((String) name)+"' ");
			   //System.out.print("'" + name + "' : ");
			   System.out.println("\n");
		   }
		}
		
		public static void main(String[] args) throws IOException {
			long start = System. currentTimeMillis();
	
			JSONObject  pub = new JSONObject();
			JSONArray data = new JSONArray();
	
			
			Document doc = Jsoup.connect("http://www.m-job.ma/").get();
			Elements lists = doc.getElementsByClass("footer-list").get(0).getElementsByClass("list");
			  for(Element list: lists) {
				  Elements items =list.getElementsByTag("a");
				  for(Element item: items) {
					  String secteur =item.text();
					  String href     = item.attr("href");
					  
					  Document page = Jsoup.connect("http://www.m-job.ma" + href).get(); 
					  Elements offres=page.getElementsByClass("offer-title");
					  
					  for(Element offre: offres) {
						  String href1=offre.getElementsByTag("a").attr("href");
						  String usrAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
						  try {
						  Document page_offre = Jsoup.connect(href1).userAgent(usrAgent).get();
							pub = new JSONObject();
							 pub.put("TitlePub",page_offre.select("h1.offer-title")==null ? null : page_offre.select("h1.offer-title").text());
							 pub.put("Secteur_activite",secteur);
						  pub.put("Entreprise",page_offre.select("ul.list-details > li").get(0).select("h3")==null ? null : page_offre.select("ul.list-details > li").get(0).select("h3").text());
					
						  pub.put("Type_Contrat",page_offre.select("ul.list-details > li").get(1).select("h3")==null ? null : page_offre.select("ul.list-details > li").get(1).select("h3").text());
						  pub.put("Salaire",page_offre.select("ul.list-details > li > h3").get(2).select("h3")==null ? null : page_offre.select("ul.list-details > li > h3").get(2).select("h3").text());
						  pub.put("Location",page_offre.select("div.location > span")==null ? null : page_offre.select("div.location > span").text());
						  pub.put("nfo_entreprise",page_offre.select("div.the-content > div").get(0)==null ? null : page_offre.select("div.the-content > div").get(0).text());
						  pub.put("Info_post",page_offre.select("div.the-content > div").get(1)==null ? null : page_offre.select("div.the-content > div").get(1).text());
						  pub.put("Profile_cherche",page_offre.select("div.the-content > div").get(2)==null ? null : page_offre.select("div.the-content > div").get(2).text());
						  pub.put("Metier",page_offre.select("div.the-content > div").get(4)==null ? null : page_offre.select("div.the-content > div").get(4).text());
						  pub.put("Experience",page_offre.select("div.the-content > div").get(5)==null ? null : page_offre.select("div.the-content > div").get(5).text());
						  pub.put("Niveau_etude",page_offre.select("div.the-content > div").get(6)==null ? null : page_offre.select("div.the-content > div").get(6).text());
						  pub.put("langue",page_offre.select("div.the-content > div").get(7)==null ? null : page_offre.select("div.the-content > div").get(7).text());
						  pub.put("Date_pub",page_offre.select("div.bottom-content > span")==null ? null : page_offre.select("div.bottom-content > span").text());
						  pub.put("Lien_offre",href1);
						  System.out.println(href);
						  System.out.println(href1);
							 
								 data.put(pub); 
						  }
						  catch(SocketException ex) {
							  continue;
							  
						  }
						  catch(IndexOutOfBoundsException ex) {
							  continue;
							  
						  }
						  
						  catch(SocketTimeoutException ex) {
							  continue;
							  
						  }
				
					  }
					  
//					   System.out.println(String.format("The page %3d was scraped successfully !", item));
					
				  }
//
//				    System.out.println(String.format("The list %3d was scraped successfully !", list.text()));
			  }
			    	
		
	
		   
		    System.out.println(data.length());
		    //System.out.println(data);
		    show(data);
		    long end = System. currentTimeMillis();
		    System.out.println("The run time of code is : " + time_format((end-start)/1000));
		    
		    Scanner sc= new Scanner(System.in);
		    System.out.print("Do you want to save data in 'data.json' file? (yes/no) ");  
		    String v = sc.nextLine();
		    while (v.compareTo("yes") != 0 && v.compareTo("no") != 0) {
		    	System.out.print("Please enter a correct response ! ");  
			    v = sc.next();
		    }
		    if(v.compareTo("yes") == 0) {
		    	try(FileWriter file = new FileWriter("data.json")){
			    	file.write(data.toString());
			    	System.out.println("The file was created successfully !");
			    }catch(IOException e) {
			    	System.out.println("Error in writing of the file !");
			    	e.printStackTrace();
			    }
		    }
		    System.out.println("End !");
		}

}
