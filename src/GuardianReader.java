import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Programm zum Herunterladen von Zeitungsartikeln ueber die Guardian API
 * @link https://open-platform.theguardian.com/access/
 * @author Marius Rosenbaum
 * Speichert anschliessend jede Seite im angegebenen Pfad 
 */
public class GuardianReader {
	
	private final static String apikey =  "<API-KEY>"; //TODO Um dieses Programm zu nutzen, wird ein API-Key der Guardian Open Platform benötigt
	private final static String path = "E:\\Bachelorarbeit Texte\\TheGuardian\\";
	
	private final static int pageSize = 50;
	private final static int pages = 1;
	
	public static void main(String[] args) throws InterruptedException {
		        
		String topics[] = {"business", "politics", "science", "sport"}; 	//business, politics, science, sport
				
		for(String topic : topics) {
			downloadArticles(GuardianReader.pageSize, GuardianReader.pages, topic);		
		}
         
    }
	
	/**
	 * Diese Methode lädt Artikel zum gegebenen Thema herunter. Bei einer {@code pagezise} von 50 und einem
	 * {@code pageAmount} von 20 werden insgesamt 1000 Zeitungsartikel heruntergeladen.
	 * @param pagesize		Wie viele Artikel pro Antwort erfragt werden (Wert zwischen 1 und 50)
	 * @param pageAmount	Wie viele Seiten angefragt werden sollen
	 * @param topic			Die Rubrick, aus der die Zeitungsartikel herunterladen werden sollen
	 * @throws InterruptedException
	 */
	private static void downloadArticles(int pagesize, int pageAmount, String topic) throws InterruptedException {
		
		StringBuilder builder = new StringBuilder();
		
		for(int i = 1; i <= pageAmount; i++) {
			try {
				URL url = new URL("https://content.guardianapis.com/"+ topic +"?show-fields=bodyText&page=" + i + "&page-size=" + pagesize + "&api-key=" + apikey);
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
				
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
				
			}
			catch (MalformedURLException e) {
				System.out.println("Fehlerhafte URL: " + e.getMessage());
			}
			catch (IOException e) {
				System.out.println("I/O Fehler: " + e.getMessage());
			}
			
			TimeUnit.MILLISECONDS.sleep(100); //Maximal 12 API-Calls pro Sekunde
			
			writeToFile(topic, builder.toString(), i);
			
			builder = new StringBuilder();

		}
	}
	
	/**
	 * Speichert die Zeitungsartikel an dem vorher angegebenen Pfad.
	 * @param topic		Die Rubrick der Zeitungsartikel
	 * @param data		Die API-Antwort im JSON-Format
	 * @param section	Die wie vielte Seite heruntergeladen wurde
	 */
	private static void writeToFile(String topic, String data, int section) {
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "articles\\" + topic + "\\" + topic + section + ".json", true), StandardCharsets.UTF_8));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			System.out.println("I/O Fehler: " + e.getMessage());
		}
	}
}
