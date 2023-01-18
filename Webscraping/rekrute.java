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

public class rekrute {

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
		//HashMap<String, String> data = new HashMap<String, String>();
		//ArrayList<String> l = new ArrayList<String>();
		
		//columns names
		ArrayList<String> columns = new ArrayList<String>();
		columns.add("titlePub"); 
		columns.add("DescriptionPub");
		columns.add("InfoPoste"); 
		columns.add("datePub_begin");
		columns.add("datePub_end"); 
		columns.add("post_prop");
		columns.add("secteur_activite");
		columns.add("fonction");
		columns.add("experience_requise"); 
		columns.add("niv_etd_dem");
		columns.add("type_ctrt_prp");
		columns.add("teletravail");
		columns.add("adresse");
		columns.add("profil_cherche");
		columns.add("Description_entreprise");
		columns.add("Trait_souhaite");
		columns.add("Site_entreprise");
		columns.add("Nb_poste");
		
		
		//data HashMap with empty arrays
		JSONObject  pub = new JSONObject();
		JSONArray data = new JSONArray();
		/*for(String column: columns) {
			pub.put(column, pub);
			data = new JSONArray();
		}*/
		int p=1, c=0, numberofpages = 156;
		Document doc = Jsoup.connect("https://www.rekrute.com/offres.html").get();
		while(p <= numberofpages) {
			doc = Jsoup.connect("https://www.rekrute.com/offres.html?s=1&p=" + p + "&o=1").get();  
		    Elements elementos = doc.getElementsByClass("post-id");
		    for(Element x: elementos) {
//		    	pub.put("titlePub",x.getElementsByTag("h2").text());
//		    	
//		    	Elements y = x.getElementsByClass("info");
//		    	//
//		    	pub.put("DescriptionPub",y.get(0).text());
//		    	//
//		    	pub.put("InfoPub",y.get(1).text());
//		    	//
//		    	pub.put("datePub_begin",x.getElementsByTag("em").get(0).getElementsByTag("span").get(0).text());
//		    	//
//		    	pub.put("datePub_end",x.getElementsByTag("em").get(0).getElementsByTag("span").get(1).text());
//		    	
//		    
		    		
		    	
		    	data.put(pub);
		    	c++;
		    	pub = new JSONObject();
		    }
		    System.out.println(String.format("The page %3d was scraped successfully !", p));
		    p++;
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
