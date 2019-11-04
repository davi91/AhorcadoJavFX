package dad.javafx.main;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import dad.javafx.ahorcado.RootController;
import dad.javafx.jugadores.Jugador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AhorcadoApp extends Application {

	private RootController root = new RootController();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = new Scene(root.getRootView(), 640, 480);
		
		primaryStage.setTitle("Juego del ahoracdo");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	@Override
	public void stop() throws Exception {
		
		// Guardamos todos los datos de jugadores y palabras
		guardarPalabras();
		guardarJugadores();
	}
	
	private void guardarPalabras() {
		
		FileOutputStream file = null;
		OutputStreamWriter out = null;
		BufferedWriter writer = null;
		
		try {
			
			file = new FileOutputStream(getClass().getResource(RootController.PALABRASURL).getFile());
			out = new OutputStreamWriter(file, StandardCharsets.UTF_8);
			writer = new BufferedWriter(out);
			
			// Todas las palabras estarán en mayúsculas, si de antes había un fichero con otras palabras nos
			// aseguramos de que se guarden en mayúsculas
			for( String str : root.getPalabrasList()) {
				writer.write(str.toUpperCase()); // Guardamos las palabras por lineas
				writer.newLine();
			}
			
		} catch (IOException e) {
			sendFileError(RootController.PALABRASURL);
		} finally {
			
			try {	
				if( writer != null )
					writer.close();
				
				if( out != null )
					out.close();
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(RootController.PALABRASURL);
			}
		}
	}
	
	private void guardarJugadores() {
		
		FileOutputStream file = null;
		OutputStreamWriter out = null;
		BufferedWriter writer = null;
		
		try {
			
			file = new FileOutputStream(getClass().getResource(RootController.JUGADORESURL).getFile());
			out = new OutputStreamWriter(file, StandardCharsets.UTF_8);
			writer = new BufferedWriter(out);
			
			for( Jugador j : root.getJugadoresList()) {
				writer.write(j.getNombre() + "," + j.getPuntuacion()); // Guardamos las puntuaciones en el formato adecuado
				writer.newLine();
			}
			
		} catch (IOException e) {
			sendFileError(RootController.JUGADORESURL);
		} finally {
			
			try {	
				if( writer != null )
					writer.close();
				
				if( out != null )
					out.close();
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError(RootController.JUGADORESURL);
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
