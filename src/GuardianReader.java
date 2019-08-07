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
	
	public static void main(String[] args) throws InterruptedException {
		
		String apikey =  "<API-KEY>"; //TODO Um dieses Programm zu nutzen, wird ein API-Key der Guardian Open Platform benötigt
		String section = "science"; 	//business, politics, science, sport
		String path = "E:\\Bachelorarbeit Texte\\TheGuardian\\";
        
		StringBuilder builder = new StringBuilder();
		
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		//Laed die ersten 200 Seiten mit jeweils 50 Artikel runter - Zum Training des Klassifikators
		int pageSize = 50; 	//Anzahl der Artikel pro Seite [1-50]
		int pages = 200;	//Anzahl der Seiten 
		
		for(int i = 1; i <= pages; i++) {
			try {
				URL url = new URL("https://content.guardianapis.com/"+ section +"?show-fields=bodyText&page=" + i + "&page-size=" + pageSize + "&api-key=" + apikey);
				
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
			
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "training\\" + section + "\\" + section + i + ".json", true), StandardCharsets.UTF_8));
				writer.write(builder.toString());
				writer.close();
			} catch (IOException e) {
				System.out.println("I/O Fehler: " + e.getMessage());
			}
			
			builder = new StringBuilder();
		}
		//--------------------------------------------------------------------------------------------------------------------------------------------------------------

		//--------------------------------------------------------------------------------------------------------------------------------------------------------------
		//Laed die Seiten 201 - 210 � 50 Artikel runter - Zum Testen des Klassifikators
		for(int i = 201; i <= 210; i++) {
			try {
				URL url = new URL("https://content.guardianapis.com/"+ section +"?show-fields=bodyText&page=" + i + "&page-size=" + pageSize + "&api-key=" + apikey);
				
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
			
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "test\\" + section + "\\" + section + i + ".json", true), StandardCharsets.UTF_8));
				writer.write(builder.toString());
				writer.close();
			} catch (IOException e) {
				System.out.println("I/O Fehler: " + e.getMessage());
			}
			
			builder = new StringBuilder();
		}
         
    }
}
