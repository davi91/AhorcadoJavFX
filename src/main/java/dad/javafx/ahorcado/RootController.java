package dad.javafx.ahorcado;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import dad.javafx.jugadores.Jugador;
import dad.javafx.jugadores.JugadoresController;
import dad.javafx.palabras.PalabrasController;
import dad.javafx.partida.PartidaController;
import dad.javafx.partida.PartidaInicioController;
import dad.javafx.partida.SeleccionJugadorDialog;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Esta clase va a ser la que gestione todo lo que tiene que ver con la carga
 * de ficheros tanto para las palbaras como las puntuaciones, además de hacer de controlador
 * principal de todos los demás Controller. El guardado de ficheros cuando se cierra la aplicación
 * la lleva la App.
 * <br><br>
 * <b> Importante: El fichero palabras.txt y jugadores.txt deben estar en la subcarpeta de recursos </b>
 * @author David Fernández Nieves
 *
 */
public class RootController {
	
	// View
	private RootView view;

	// Las demás pestañas
	private PalabrasController pController;
	private JugadoresController jController;
	private PartidaInicioController iController;
	private PartidaController playController;
	
	// Model -> En general, las distintas vistas
	private ObjectProperty<Node> gameNode = new SimpleObjectProperty<>(); // La pantalla de inicio y de juego
	private ObjectProperty<Node> palabrasNode = new SimpleObjectProperty<>(); // La pantalla de palabras
	private ObjectProperty<Node> jugadoresNode = new SimpleObjectProperty<>(); // La pantalla de jugadores
	
	// Nos sirve para bloquear las demás pestañas en caso de que se esté jugando
	private BooleanProperty enJuego = new SimpleBooleanProperty(false);
	
	public RootController() {
		
		// Cargamos por FXML todos los demás Controller
		try {
			// Aprovechando que los dos usan listas, podemos cargar los datos aquí
			pController = new PalabrasController(this);
			
			jController = new JugadoresController(this);
			
			// Ya este controlador se encarga de cargar sus propios datos
			iController = new PartidaInicioController(this);
			
		} catch (IOException e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR FATAL");
			alert.setHeaderText("No se pudo inicializar correctamente la aplicación");
			alert.showAndWait();
			Platform.exit();
		}
		
		// Por último, cargamos la vista principal
		view = new RootView();
		view.getTab_game().contentProperty().bind(gameNode);
		view.getTab_puntuaciones().contentProperty().bind(jugadoresNode);
		view.getTab_palabras().contentProperty().bind(palabrasNode);
		
		view.getTab_puntuaciones().disableProperty().bind(enJuego);
		view.getTab_palabras().disableProperty().bind(enJuego);
		
		gameNode.set(iController.getRootView()); // Empezamos con la vista inicial
		jugadoresNode.set(jController.getRootView());
		palabrasNode.set(pController.getRootView());
	}
	
	
	/**
	 * Principal fallo a la hora de procesar un fichero, ya sea porque
	 * no tiene el contenido correcto o porque no se puede acceder a el
	 * @param fileName Nombre o rut adel fichero
	 */
	public void sendFileError(String fileName) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("Error al procesar el fichero: " + fileName);
		alert.setContentText("Compruebe que el fichero está en la subcarpeta resources y no esté abierto por ningún otro programa");
		alert.showAndWait();
		Platform.exit();
	}
	
	/**
	 * Inicamos la partida, cargamos un nuevo contenido.
	 */
	public void iniciarPartida() {
		
		SeleccionJugadorDialog dialog = new SeleccionJugadorDialog(jController.listaJugadorProperty());
		Optional<Jugador> j = dialog.showAndWait();
		
		if( j.isPresent() ) {
			
			try {
				playController = new PartidaController(this);
				gameNode.set(playController.getRootView());
				playController.setJugadorActual(j.get());
				
				// Bloqueamos las demás pestañas
				enJuego.set(true);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Se ha acabado la partida, volvemos a establecer 
	 * la vista por defecto
	 */
	public void finPartida() {
		
		// Volvemos a habilitar las pestañas
		enJuego.set(false);
		
		try {
			iController = new PartidaInicioController(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		gameNode.set(iController.getRootView());
	}
	
	public RootView getRootView() {
		return view;
	}

	/**
	 * Lista de palabras.
	 * @return La lista de palabras actual.
	 */
	public ArrayList<String> getPalabrasList() {
		return new ArrayList<>(pController.getList());
	}

	/**
	 * Lista de jugadores.
	 * @return La lista de jugadores actual.
	 */
	public ArrayList<Jugador> getJugadoresList() {
		return new ArrayList<>(jController.getListaJugador());
	}

	public final ObjectProperty<Node> gameNodeProperty() {
		return this.gameNode;
	}
	
	/**
	 * Ajustamos la lista de jugadores
	 * @param jList ArrayList de jugadores
	 */
	public void setJugadoresList(ArrayList<Jugador> jList) {
		jController.setListaJugador(FXCollections.observableArrayList(jList));
	}
	
	/**
	 * Ajustamos la lista de palabras
	 * @param pList ArrayList de Strings conteniendo las palabras
	 */
	public void setPalabrasList(ArrayList<String> pList) {
		pController.setList(FXCollections.observableArrayList(pList));
	}

	public final Node getGameNode() {
		return this.gameNodeProperty().get();
	}
	

	public final void setGameNode(final Node gameNode) {
		this.gameNodeProperty().set(gameNode);
	}
	

	public final ObjectProperty<Node> palabrasNodeProperty() {
		return this.palabrasNode;
	}
	

	public final Node getPalabrasNode() {
		return this.palabrasNodeProperty().get();
	}
	

	public final void setPalabrasNode(final Node palabrasNode) {
		this.palabrasNodeProperty().set(palabrasNode);
	}
	

	public final ObjectProperty<Node> jugadoresNodeProperty() {
		return this.jugadoresNode;
	}
	

	public final Node getJugadoresNode() {
		return this.jugadoresNodeProperty().get();
	}
	

	public final void setJugadoresNode(final Node jugadoresNode) {
		this.jugadoresNodeProperty().set(jugadoresNode);
	}
	
	
}
