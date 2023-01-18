package Webscraping;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import org.jsoup.Jsoup;  
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONArray;
import org.json.JSONObject;

public class emploi_ma_webscraping {
	
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
	    	System.out.print("'" + l.get(i)+"', ");
	    	i++;
	    }
	}
	
	public static void show(JSONObject l) {
	   for(Object name: l.names()) {
		   System.out.print("'" + name + "' : ");
		   show(l.getJSONArray((String) name));
		   System.out.println("\n");
	   }
	}
	
	public static void main(String[] args)throws IOException {
		long start = System.currentTimeMillis();
		
		//columns names
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("titlepub");           columns.add("datepub");
		columns.add("Name entreprise");    columns.add("Description de l'entreprise");
		columns.add("InfoPub");            columns.add("post_prop");
		columns.add("nbr_post_prop");      columns.add("secteur_activite");
		columns.add("fonction");           columns.add("experience_requise"); 
		columns.add("niv_etd_dem");        columns.add("type_ctrt_prp");
		
		//data HashMap with empty arrays
		JSONObject  data = new JSONObject();
		JSONArray pub = new JSONArray();
		for(String column: columns) {
			data.put(column, pub);
			pub = new JSONArray();
		}

		
		Document doc, post_page;
		String href, id_pub, xpath, root;
		String[] spliter;
		String nbr_post_prop;
		
		int p=0, c=0;
		while(p<=46) {
			//Page content
			if( p == 0) {
				doc = Jsoup.connect("https://www.emploi.ma/").get();
				root="";
			}else {
				doc = Jsoup.connect("https://www.emploi.ma/recherche-jobs-maroc?page=" + p).get();
				root="https://www.emploi.ma";
			}
			Elements elementos = doc.getElementsByClass("job-description-wrapper");
			for(Element x: elementos) {
				
				pub = data.getJSONArray("titlepub");
				if (x.getElementsByTag("a").size() >=2) {
					pub.put(c,x.getElementsByTag("a").get(1).text());
					href     = root + x.getElementsByTag("a").get(1).attr("href");
				}else {
					pub.put(c,x.getElementsByTag("a").text());
					href     = root + x.getElementsByTag("a").attr("href");
				}
		    	
		    	
		    	pub = data.getJSONArray("datepub");
		    	pub.put(c,x.getElementsByClass("job-recruiter").get(0).text());
			
				//Post content
				//System.out.println("href = "+href);
				spliter  = href.split("-");
				id_pub         = spliter[spliter.length-1];
				
				post_page = Jsoup.connect(href).get();
				
				xpath="#company-profile-" + id_pub +" > div  > div  > div > .company-title > a";
				pub = data.getJSONArray("Name entreprise");
		    	pub.put(c,post_page.select(xpath).text());
				
				xpath="#company-profile-" + id_pub +" > div  > div > .job-ad-company-description";
				pub = data.getJSONArray("Description de l'entreprise");
				pub.put(c,post_page.select(xpath).text().replace(post_page.select(xpath + " > label").text(), ""));
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > div > span";
				pub = data.getJSONArray("post_prop");
		    	pub.put(c,post_page.select(xpath).text().replace("Poste proposé : ", ""));
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > table > tbody > tr:eq(9) > td:eq(1)";
				nbr_post_prop = post_page.select(xpath).text();
				if(nbr_post_prop.isEmpty()) {
					nbr_post_prop = "None";
				}
				pub = data.getJSONArray("nbr_post_prop");
		    	pub.put(c,nbr_post_prop);

				xpath="#company-profile-" + id_pub +" > div  > div > div:eq(0) > table > tbody > tr:eq(1) > td:eq(1) > div";
				pub = data.getJSONArray("secteur_activite");
		    	pub.put(c,post_page.select(xpath+" > div:eq(0)").text());
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > table > tbody > tr:eq(0) > td:eq(1) > div > div > div";
				pub = data.getJSONArray("fonction");
		    	pub.put(c,post_page.select(xpath).text());
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > table > tbody > tr:eq(5) > td:eq(1) > div > div > div";
				pub = data.getJSONArray("experience_requise");
		    	pub.put(c,post_page.select(xpath).text());
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > table > tbody > tr:eq(6) > td:eq(1) > div > div > div";
				pub = data.getJSONArray("niv_etd_dem");
		    	pub.put(c,post_page.select(xpath).text());
				
				xpath="#job-ad-details-" + id_pub +" > div  > div > table > tbody > tr:eq(2) > td:eq(1) > div > div > div";
				pub = data.getJSONArray("type_ctrt_prp");
		    	pub.put(c,post_page.select(xpath).text());
				c++;
			}
			System.out.println(String.format("The page %3d was scraped successfully !", p));
			p++;
		}
		System.out.println(data.length());
		show(data);
		long end = System. currentTimeMillis();
	    System.out.println("The run time of code is : " + time_format((end-start)/1000));
	    
	    Scanner sc= new Scanner(System.in);
	    System.out.print("Do you want to save data in 'data2.json' file? (yes/no) ");  
	    String v = sc.nextLine();
	    while (v.compareTo("yes") != 0 && v.compareTo("no") != 0) {
	    	System.out.print("Please enter a correct response ! ");  
		    v = sc.next();
	    }
	    if(v.compareTo("yes") == 0) {
	    	try(FileWriter file = new FileWriter("data2.json")){
		    	file.(data.toString());
		    	System.out.println("The file was created successfully !");
		    }catch(IOException e) {
		    	System.out.println("Error in writing of the file !");
		    	e.printStackTrace();
		    }
	    }
	    System.out.println("End !");
	}
}

