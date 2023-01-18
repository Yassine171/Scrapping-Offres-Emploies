	package Webscraping;
	import java.io.File;
import java.io.FileWriter;
	import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
	import org.jsoup.Jsoup;  
	import org.jsoup.nodes.Document;
	import org.jsoup.nodes.Element;
	import org.jsoup.select.Elements;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
	import org.json.JSONObject;
	
	import java.io.IOException;
	import java.util.*;
	import org.jsoup.Jsoup;  
	import org.jsoup.nodes.Document;
	import org.jsoup.nodes.Element;
	import org.jsoup.select.Elements;
	import org.json.JSONArray;
	import org.json.JSONObject;
	
public class Train {
			public static void main(String[] args) throws IOException  {
					//long start = System. currentTimeMillis();
					System.out.println("hello");
		
					// Establish connection to website and get HTML content
					Document doc = Jsoup.connect("https://www.m-job.ma/offres-emploi/115302-casablancaconseillers-commerciaux-en-reception-dappels-5-casablanca").get();

					// Select the elements you want to scrape
					Elements elements = doc.select("p");

					// Create a CSV printer
					File file = new File("data.csv");
					try (FileWriter out = new FileWriter(file);
					     CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT)) {

					    // Iterate through the elements and write data to the CSV file
					    for (Element element : elements) {
					        String text = element.text();
					     //   String attribute = element.attr("attribute");
					        printer.printRecord(text);
					    }
					} catch (IOException e) {
					    e.printStackTrace();
					}


		}
}