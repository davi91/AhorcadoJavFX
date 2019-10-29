package dad.javafx.ahorcado;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import dad.javafx.palabras.PalabrasController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Esta clase va a ser la que gestione todo lo que tiene que ver con la carga
 * de ficheros tanto para las palbaras como las puntuaciones, además de hacer de controlador
 * principal de todos los demás Controller. El guardado de ficheros cuando se cierra la aplicación
 * la lleva la App.
 * <br><br>
 * <b> Importante: El fichero palabras.txt y puntuaciones.txt deben estar en la subcarpeta de recursos </b>
 * @author David Fernández Nieves
 *
 */
public class RootController {

	// View
	private RootView view;
	
	// Model
	private ObservableList<String> oList =  FXCollections.observableArrayList(new ArrayList<String>());

	// Las demás pestañas
	private PalabrasController pController;
	
	public RootController() {

		// Cargamos los ficheros de datos que contienen las palabras y los jugadores
		cargarPalabras();
		
		// Cargamos por FXML todos los demás Controller
		try {

			pController = new PalabrasController(this);
			
		} catch (IOException e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR FATAL");
			alert.setHeaderText("No se pudo inicializar correctamente la aplicación");
			alert.showAndWait();
			Platform.exit();
		}
		
		// Por último, cargamos la vista principal
		view = new RootView(pController.getBorderRoot());
	}
	
	private void cargarPalabras() {
		
		FileInputStream file = null;
		InputStreamReader in = null;
		BufferedReader reader = null;
		
		try {
			
		//	file = new FileInputStream(getClass().getResource("/text/palabras.txt").toString());
			file = new FileInputStream("palabras.txt");
			in = new InputStreamReader(file, StandardCharsets.UTF_8);
			reader = new BufferedReader(in);
			
			String line;
			while( (line = reader.readLine()) != null ) {
				
				// Limpiamos los espacios
				line.trim();
				
				// Vamos cargando las palabras en la lista
				oList.add(line);
			}
			
		} catch (IOException e) {
			sendFileError("palabras.txt");
		} finally {
			
			try {	
				if( reader != null )
					reader.close();
				
				if( in != null )
					in.close();
				
				if( file != null )
					file.close();
				
			} catch (IOException e) {
				sendFileError("palabras.txt");
			}
		}
	}
	
	private void sendFileError(String fileName) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("Error al procesar el fichero:" + fileName);
		alert.setContentText("Compruebe que el fichero está en la subcarpeta resources y no esté abierto por ningún otro programa");
		alert.showAndWait();
		Platform.exit();
	}
	
	public RootView getRootView() {
		return view;
	}
	
	public ObservableList<String> getoList() {
		return oList;
	}
}
