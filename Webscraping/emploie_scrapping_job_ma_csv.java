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
	
public class emploie_scrapping_job_ma_csv {

	   public static void main(String[] args) throws IOException {
	        // Connect to the website and retrieve the HTML content
	        String url = "https://www.m-job.ma/offres-emploi/115310-conseillerscommerciaux-parlant-francais-57-rabat";
	        Document doc = Jsoup.connect(url).get();

	        // Extract data from the HTML document
	        Elements jobTitles = doc.select("h2.job-title");
	        Elements companies = doc.select("h3.company");
	        Elements locations = doc.select("span.location");

	        // Create a new CSV file and write the data to it
	        Writer writer = new FileWriter("jobs.csv");
	        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
	                                                    .withHeader("Job Title", "Company", "Location"));
	        for (int i = 0; i < jobTitles.size(); i++) {
	            Element jobTitle = jobTitles.get(i);
	            Element company = companies.get(i);
	            Element location = locations.get(i);
	            printer.printRecord(jobTitle.text(), company.text(), location.text());
	        }
	        printer.flush();
	        printer.close();
	    }
	}

}
	
	