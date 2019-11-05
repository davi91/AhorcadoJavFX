package dad.javafx.palabras;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;

/**
 * Controlador de la pestaña de "palabras" que básicamente consiste en una lista donde
 * se nos muestran las palabras del archivo palabras.txt y se van a procesar en el juego.
 * Se tienen implementados los métodos para insertar y quitar palabras de la lista, cualquier
 * cambio en ella se quedará guardado al cerrar la aplicación.
 * 
 * @author David Fernández Nieves
 *
 */
public class PalabrasController implements Initializable {

	
	// FXML Related : View
	//----------------------------------------------------------
	
	@FXML
	private Button addBt, removeBt;
	
	@FXML
	private BorderPane borderRoot;

	@FXML
	private ListView<String> palabrasList;	
	
	//----------------------------------------------------------
	
	// Model
	private ObservableList<String> oList =  FXCollections.observableArrayList(new ArrayList<String>());
	private ListProperty<String> list = new SimpleListProperty<>(oList); 
	
	private StringProperty palabraSelected = new SimpleStringProperty();
	
	/**
	 * 
	 * @param parentRoot El controlador principal
	 * @throws IOException Error en la carga del FXML PalabrasFXML.fxml
	 */
	public PalabrasController() throws IOException {
				
		FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/PalabrasFXML.fxml"));
		loader.setController(this);
		loader.load();
	
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Bindings
		palabrasList.itemsProperty().bind(list);
		removeBt.disableProperty().bind( palabrasList.getSelectionModel().selectedItemProperty().isNull() );
		palabraSelected.bind( palabrasList.getSelectionModel().selectedItemProperty());
		
		// Eventos
		addBt.setOnAction( evt -> insertarPalabra() );
		removeBt.setOnAction( evt -> quitarPalabra() );
	}
	
	/**
	 * Insertamos una nueva palabra en la lista. La palabra
	 * debe tener al menos 3 caracteres.
	 */
	private void insertarPalabra() {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Insertar palabra");
		dialog.setHeaderText("Inserte una nueva palabra");
		dialog.setContentText("La palabra debe tener al menos 3 caracteres");
		
		Optional<String> palabra = dialog.showAndWait();

		if( palabra.isPresent() && palabra.get().length() < 3) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Palabra erronea");
			alert.setHeaderText("La palabra debe tener al menos 3 caracteres");
			alert.showAndWait();
			
		} else if( palabra.isPresent() ) {
			list.add(palabra.get().toUpperCase()); // Todas las palabras estarán en mayúsculas
		}
	}
	
	/**
	 * Quitamos la palabra seleccionada en la lista.
	 */
	private void quitarPalabra() {
		
		// Ya sabemos que este botón solo estará activo si se ha 
		// seleccionado una palabra, pero esta bien ser precavido.
		if( palabraSelected.get() != null ) {
			list.remove(palabraSelected.get());
		}
	}
	
	public BorderPane getRootView() {
		return borderRoot;
	}

	public final ListProperty<String> listProperty() {
		return this.list;
	}
	

	public final ObservableList<String> getList() {
		return this.listProperty().get();
	}
	

	public final void setList(final ObservableList<String> list) {
		this.listProperty().set(list);
	}
	
	

}
