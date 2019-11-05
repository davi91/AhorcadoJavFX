package dad.javafx.jugadores;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

public class JugadoresController implements Initializable {

	
	// FXML : View
	//-----------------------------------------------------
	
	@FXML
	private BorderPane view;
	
	@FXML
	private TableView<Jugador> playersTable;
	
	//-----------------------------------------------------
	
	// Model
	private ObservableList<Jugador> jList = FXCollections.observableArrayList(new ArrayList<Jugador>());
	private ListProperty<Jugador> list =  new SimpleListProperty<>(jList);;
	
	public JugadoresController() throws IOException {
				
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/JugadoresFXML.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		playersTable.itemsProperty().bind(list);
	}

	public final ListProperty<Jugador> listaJugadorProperty() {
		return this.list;
	}
	

	public final ObservableList<Jugador> getListaJugador() {
		return this.listaJugadorProperty().get();
	}
	

	public final void setListaJugador(final ObservableList<Jugador> listaJugador) {
		this.listaJugadorProperty().set(listaJugador);
	}
	
	public BorderPane getRootView() {
		return view;
	}
	

}
