package dad.javafx.partida;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javafx.ahorcado.RootController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Este controlador se encarga de mostrar la pantalla de inicio del juego,
 * en el que se muestra una ayuda junto con la opción de empezar la partida.
 * <br>
 * <br>
 * <b>Importante: El fichero de imagen helpIcon.png y el fichero te texto helpText.txt deben
 * estar en la subcarpeta de recursos.</b>
 * @author David Fernández Nieves
 *
 */
public class PartidaInicioController implements Initializable {

	private RootController rootController;
	
	private static final String IMAGEURL = "/images/helpIcon.png";
	private static final String HELPURL = "/text/helpText.txt";
	
	// FXML : View
	//---------------------------------------------------------------
	
	@FXML
	private VBox view;
	
	@FXML 
	private Button initBt;
	
	@FXML
	private ImageView helpIcon;
	
	@FXML
	private Text helpText;
	
	//---------------------------------------------------------------
	
	// Model
	//---------------------------------------------------------------
	
	private ObjectProperty<Image> image = new SimpleObjectProperty<>();
	
	private StringProperty help = new SimpleStringProperty();
	
	//---------------------------------------------------------------
	
	public PartidaInicioController(RootController rootController) throws IOException {
		
		this.rootController = rootController;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaInicioFXML.fxml"));
		loader.setController(this);
		loader.load();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Ajustamos los diferentes componentes de la vista, para ello usamos el modelo
		
		
		helpIcon.imageProperty().bind(image);
		
		try {
			
			image.set( new Image(getClass().getResource(IMAGEURL).toString()));
			
		} catch (NullPointerException | IllegalArgumentException e) {
			
			rootController.sendFileError(IMAGEURL);
		}
		
		helpText.textProperty().bind(help);
		loadTextInfo();
		
		initBt.setOnAction( evt -> onInitAction());
		
	}
	
	/**
	 * Iniciamos la partida, para ello avisamos al root
	 * de que vamos a cargar el contenido del juego.
	 */
	private void onInitAction() {
		rootController.iniciarPartida();
	}

	/**
	 * Cargamos el fichero de ayuda.
	 */
	private void loadTextInfo() {
		
		FileReader reader = null;
		BufferedReader buffer = null;
		
		try {
			
			reader = new FileReader(getClass().getResource(HELPURL).getFile());
			buffer = new BufferedReader(reader);
			
			String line;
			StringBuilder myText = new StringBuilder();
			
			while( (line = buffer.readLine()) != null ) {
				myText.append(line);
			}
			
			help.set(myText.toString());
			
		} catch (IOException e) {
			rootController.sendFileError(HELPURL);
		} finally {
			
			try {
				if( buffer != null ) {
					buffer.close();
				}
				
				if( reader != null ) {
					reader.close();
				}
			} catch (IOException e) {
				rootController.sendFileError(HELPURL);
			}
		}
		
	}
	
	public VBox getRootView() {
		return view;
	}

}
