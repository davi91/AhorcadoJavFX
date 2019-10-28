package dad.javafx.palabras;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

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
	private ListProperty<String> list; // El observable list del RootController tiene la lista principal por la cual se va a manejar esta
	
	public PalabrasController(ObservableList<String> pList) throws IOException {
		
		list = new SimpleListProperty<>(pList);
		FXMLLoader loader = new FXMLLoader( getClass().getResource("/fxml/PalabrasFXML.fxml"));
		loader.setController(this);
		loader.load();
	
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		palabrasList.itemsProperty().bind(list);
		list.add("Nuevo muchacho");

	}
	
	public BorderPane getBorderRoot() {
		return borderRoot;
	}

}
