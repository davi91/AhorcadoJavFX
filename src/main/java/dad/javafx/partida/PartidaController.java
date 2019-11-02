package dad.javafx.partida;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import dad.javafx.ahorcado.RootController;
import dad.javafx.jugadores.Jugador;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

/**
 * Controlador principal de la partida, coge los datos de palabras y 
 * jugadores y los aplica al juego. Mientras el jugador está en el juego,
 * no podrá cambiar de pestaña.
 * <br>
 * <br>
 * Los puntos sólo se guardarán si el jugador ha acabdo la partida o, al menos,
 * adivinado una palabra.
 * 
 * @author David Fernández Nieves
 *
 */
public class PartidaController implements Initializable {

	private RootController rootController;
	
	// Algunas constantes útiles
	private static final int VIDAS = 9;

	// Letras que se van a mostrar atendindo al tamaño de la palabra, una forma sencilla de hacerlo.
	private static final int T_PEQUEÑO = 1;
	private static final int T_MEDIO = 2;
	private static final int T_GRANDE = 3;
	
	// Si se acierta la palabra sin completar las letras
	private static final int ADIVINAR_PUNTOS = 10;
	
	/**
	 * Esta variable se irá recalculando a medida que acertamos
	 * palabras.
	 */
	private int palabraActual = 0;
	
	/**
	 * La lista de palabras obtenidas del controlador de palabras.
	    Esta variable sólo se carga cuando se inicia la partida.
	 */
	private ArrayList<String> listaPalabras;
	
	/**
	 * El jugador actual en la partida
	 */
	private Jugador jugadorActual;
	
	// FXML : View
	//----------------------------------------------------
	
	@FXML
	private GridPane view;
	
	@FXML
	private ImageView ahorcadoImg;
	
	@FXML
	private Text puntosTxt, vidasTxt;
	
	@FXML
	private Label letrasTxt, finalTxt;
	
	@FXML
	private TextField intentoTxt;
	
	@FXML
	private Button letraBt, respuestaBt;
	
	//----------------------------------------------------
	
	// Model
	private PartidaModel model = new PartidaModel();
	
	public PartidaController(RootController rootController) throws IOException {
		
		this.rootController = rootController;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaFXML.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Bindings
		ahorcadoImg.imageProperty().bind(model.imageProperty());
		puntosTxt.textProperty().bindBidirectional(model.puntosProperty(), new NumberStringConverter());
		vidasTxt.textProperty().bindBidirectional(model.vidasProperty(), new NumberStringConverter());
		letrasTxt.textProperty().bind(model.usadoProperty());
		finalTxt.textProperty().bind(model.adivinarProperty());
		model.intentoProperty().bind(intentoTxt.textProperty());
		
		// Cada vez que se cambie una vida, establecemos un cambio en la imagen
		model.vidasProperty().addListener( (v, ov, nv) -> changeImage());
		
		// Eventos
		letraBt.setOnAction( evt -> addLetra() );
		respuestaBt.setOnAction( evt -> intentoAdivinar() );
		
		// Establecemos las condiciones iniciales
		iniciarPartida();
	}
	
	/**
	 * Cambiamos la imagen atendiendo al número de vidas que tiene ( 0 -> 9 )
	 */
	private void changeImage() {
		
		if( model.getVidas() > 0 ) {
			model.setImage(new Image(getClass().getResource(String.format("/images/%d.png",10-model.getVidas())).toString()));
		}
	}
	
	/**
	 * Se carga la primera palabra y procedemos
	 * a cargar las vidas del jugador
	 */
	private void iniciarPartida() {
		model.setVidas(VIDAS);
		model.setPuntos(0);
		
		listaPalabras = rootController.getPalabrasList();
		palabraActual = (int) (Math.random() * listaPalabras.size());
		iniciarPalabra();
	}
	
	/**
	 * Establecemos la palabra a adivinar. Se le da
	 * bastante aleatoriedad al número de letras mostradas manteniendo fijo un 
	 * máximo de letras visibles.
	 */
	private void iniciarPalabra() {
		
		final String palabraOriginal =  listaPalabras.get(palabraActual);
		
		// Cambiamos los caracteres, en este caso sólo las letras
		StringBuilder palabra = new StringBuilder(palabraOriginal.replaceAll("[a-z,A-Z,0-9]", "-"));
		
		int len = palabra.length();
		int n_letras;

		// Establecemos el número de letras a mostrar como máximo
		if( len < 5 ) {
			n_letras = T_PEQUEÑO;
		} else if( len < 8 ) {
			n_letras = T_MEDIO;
		} else {
			n_letras = T_GRANDE;
		}
		
		/* 
		 * Ahora vamos obteniendo al azar el char a hacer visible, 
		 * con la posibilidad de repetir el mismo char, haciéndolo
		 * más impredecible.
		 */
		while( n_letras > 0 ) {
			
			int r = (int) (Math.random() * palabra.length());
			palabra.setCharAt(r, palabraOriginal.charAt(r));
			n_letras--;
		}
		
		model.setAdivinar(palabra.toString());
	}
	
	/**
	 * Añade una letra para comprobar si está en la palabra
	 * o no
	 */
	private void addLetra() {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Letra");
		dialog.setHeaderText("Introduce una letra");
		
		Optional<String> str = dialog.showAndWait();
		Character c = ' ';
		if( str.isPresent() && !str.get().isEmpty() && !str.get().isBlank() ) {
			
			/*
			 * Sólo cogemos el primer char en caso de que el usuario haya introducido una frase,
			 * aunque se puede poner un límite al número de chars de un TextField mediante listener,
			 * no vamos a llegar tan lejos para ésto.
			 */
			c = str.get().charAt(0);
			c = Character.toUpperCase(c); // Las palabras son en mayúscula, para que se vean más claras
			
			if( !Character.isLetter(c) && !Character.isDigit(c) ) {
				sendLetterWarning();
				return;
			}
		}
		
		else {
			sendLetterWarning();
			return;
		}
		
		final String palabraOriginal = listaPalabras.get(palabraActual);
		
		// Hay que evitar las letras repetidas
		if( model.getUsado().indexOf(c) != -1 || model.getAdivinar().indexOf(c) != -1 ) {
			return;
		}
		
		if( palabraOriginal.indexOf(c) != -1) {
			
			// Va por buen camino
			model.setPuntos(model.getPuntos()+1);
			StringBuilder palabra = new StringBuilder(model.getAdivinar());
			int i, j;
			j = 0;
			while( (i = palabraOriginal.indexOf(c,j)) != -1 ) {
				palabra.setCharAt(i, c);
				j = i+1;
			}
			
			model.setAdivinar(palabra.toString());
			
			/*
			 *  Ahora comprobamos si se ha completado la palabra o no,
			 *  si se ha completado al usuario no le damos los 10 puntos
			 *  extra por acertarla sin haberlo intentado adivinar faltando
			 *  letras.
			 */
			if( palabra.toString().compareTo(palabraOriginal) == 0 ) {
				acabarJuego(false);
			}
			
		} else {
			
			// Oops....
			model.setVidas(model.getVidas()-1);
			
			if( model.getVidas() <= 0 ) { // Hemos acabado
				fin();
			} else {
				model.setUsado(model.getUsado()+c);
			}
		}
		
	}
	
	/**
	 * Cuando se completa una palabra
	 * @param esAdivinado Si la palabra se ha adivinado sin completar las letras
	 */
	private void acabarJuego(boolean esAdivinado) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Fin");
		
		if( !esAdivinado ) {
			alert.setHeaderText("Palabra completada");
		} else {
			alert.setHeaderText("Palabra adivinada");
			model.setPuntos(model.getPuntos()+ADIVINAR_PUNTOS); // 10 puntos extras por adivinar la palabra
		}
		
		alert.showAndWait();
		// Ahora vamos a escoger una nueva palabra si hay
		listaPalabras.remove(listaPalabras.get(palabraActual));
		
		// Comprobamos si hay más palabras
		if( listaPalabras.size() == 0 ) {
			fin();
		} else {
			palabraActual = (int) (Math.random() * listaPalabras.size());
			model.setUsado(""); // Limpiamos las letras usadas
			iniciarPalabra(); // Volvemos a jugar
		}
	}
	
	/**
	 * Se acabó definitivamente la partida, guardamos
	 * los datos del jugador
	 */
	private void fin() {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Fin de juego");
		alert.setHeaderText("Se ha acabado el juego");
		
		// Procedemos a guardar los puntos del jugador
		if( jugadorActual.getPuntuacion() < model.getPuntos() ) {
			
			alert.setContentText("Su puntación total ha sido de " + model.getPuntos() + "." +
								 "\nHa superado su anterior puntuación");
			
			jugadorActual.setPuntuacion(model.getPuntos());
		}
		
		else {
			alert.setContentText("Su puntación total ha sido de " + model.getPuntos() + ".");
		}
		
		alert.showAndWait();
		
		// Avisamos de que la partida ha terminado
		rootController.finPartida();
	}
	
	/**
	 * El jugador se arriesga e inenta adivinar
	 * la palabra o frase
	 */
	private void intentoAdivinar() {
		
		if( model.getIntento().equalsIgnoreCase(listaPalabras.get(palabraActual))) {
			acabarJuego(true);
			
		} else {
			
			model.setVidas(model.getVidas()-1);
			if( model.getVidas() <= 0 ) { 
				fin();
			} 
		}
	}
	
	/**
	 * Una alerta por si la letra no es un carácter o dígito válido.
	 */
	private void sendLetterWarning() {
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Letra");
		alert.setHeaderText("Letra no válida");
		alert.setContentText("La letra introducida no es válida, vuelva a intentarlo");	
		alert.showAndWait();
	}
	
	public void setJugadorActual(Jugador jugadorActual) {
		this.jugadorActual = jugadorActual;
	}
	
	public GridPane getRootView() {
		return view;
	}

}
