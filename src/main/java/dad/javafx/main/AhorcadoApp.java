package dad.javafx.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import dad.javafx.ahorcado.RootController;
import dad.javafx.jugadores.Jugador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {


	public static final String PALABRASURL = "/text/palabras.txt";
	public static final String JUGADORESURL = "/text/jugadores.txt";

	private RootController rootController = new RootController();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = new Scene(rootController.getRootView(), 640, 480);
		
		primaryStage.setTitle("Juego del ahoracdo");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	@Override
	public void init() throws Exception {
		super.init();
		
		// Cargamos los datos de la App
		cargarJugadores();
		cargarPalabras();
	}

	@Override
	public void stop() throws Exception {
		
		super.stop();
		
		// Guardamos todos los datos de jugadores y palabras
		guardarPalabras();
		guardarJugadores();
	}
	
	/**
	 * Cargamos los datos de los jugadores y los situamos correctamente en la lista.
	 */
	private void cargarJugadores() {
		
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		ArrayList<Jugador> jList = new ArrayList<>();
		
		try {
			
			file = new FileInputStream(getClass().getResource(JUGADORESURL).getFile());
			in = new InputStreamReader(file, StandardCharsets.UTF_8);
			reader = new BufferedReader(in);
			
			String line;
			while( (line = reader.readLine()) != null ) {
				
				// Limpiamos los espacios
				line.trim();
				String[] jArray = line.split(",");
				jList.add(new Jugador(jArray[0], Integer.parseInt(jArray[1])));
			}
			
			rootController.setJugadoresList(jList);
			
		} catch (IOException | NumberFormatException e) {
			sendFileError(JUGADORESURL);
		} finally {
			
			try {	
				
				if( reader != null )
					reader.close();
				
				if( in != null ) {
					in.close();
				}
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(JUGADORESURL);
			}
		}
	}
	
	/**
	 * Cargamos los datos de las palabras y los situamos correctamente en la lista.
	 */
	private void cargarPalabras() {
		
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		ArrayList<String> pList = new ArrayList<>();
		
		try {
			
			file = new FileInputStream(getClass().getResource(PALABRASURL).getFile());
			in = new InputStreamReader(file, StandardCharsets.UTF_8);
			reader = new BufferedReader(in);
			
			String line;
			while( (line = reader.readLine()) != null ) {
				
				// Limpiamos los espacios
				line.trim();

				pList.add(line);
			}
			
			rootController.setPalabrasList(pList);
			
		} catch (IOException | NumberFormatException e) {
			sendFileError(PALABRASURL);
		} finally {
			
			try {	
				
				if( reader != null )
					reader.close();
				
				if( in != null ) {
					in.close();
				}
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(PALABRASURL);
			}
		}
	}
	
	private void guardarPalabras() {
		
		FileOutputStream file = null;
		OutputStreamWriter out = null;
		BufferedWriter writer = null;
		
		try {
			
			file = new FileOutputStream(getClass().getResource(PALABRASURL).getFile());
			out = new OutputStreamWriter(file, StandardCharsets.UTF_8);
			writer = new BufferedWriter(out);
			
			// Todas las palabras estarán en mayúsculas, si de antes había un fichero con otras palabras nos
			// aseguramos de que se guarden en mayúsculas
			for( String str : rootController.getPalabrasList()) {
				writer.write(str.toUpperCase()); // Guardamos las palabras por lineas
				writer.newLine();
			}
			
		} catch (IOException e) {
			sendFileError(PALABRASURL);
		} finally {
			
			try {	
				if( writer != null )
					writer.close();
				
				if( out != null )
					out.close();
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(PALABRASURL);
			}
		}
	}
	
	private void guardarJugadores() {
		
		FileOutputStream file = null;
		OutputStreamWriter out = null;
		BufferedWriter writer = null;
		
		try {
			
			file = new FileOutputStream(getClass().getResource(JUGADORESURL).getFile());
			out = new OutputStreamWriter(file, StandardCharsets.UTF_8);
			writer = new BufferedWriter(out);
			
			for( Jugador j : rootController.getJugadoresList()) {
				writer.write(j.getNombre() + "," + j.getPuntuacion()); // Guardamos las puntuaciones en el formato adecuado
				writer.newLine();
			}
			
		} catch (IOException e) {
			sendFileError(JUGADORESURL);
		} finally {
			
			try {	
				if( writer != null )
					writer.close();
				
				if( out != null )
					out.close();
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(JUGADORESURL);
			}
		}
	}
	
	private void sendFileError(String fileName) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("Error al guardar el fichero:" + fileName);
		alert.setContentText("Compruebe que el fichero no esté abierto por otro programa");
		alert.showAndWait();
	}
	

	public static void main(String[] args) {
		launch(args);

	}

}
