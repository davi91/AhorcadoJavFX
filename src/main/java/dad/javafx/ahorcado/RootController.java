package dad.javafx.ahorcado;

import java.io.IOException;
import java.util.ArrayList;

import dad.javafx.palabras.PalabrasController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RootController {

	// View
	private RootView view;
	
	// Model
	private ObservableList<String> oList =  FXCollections.observableArrayList(new ArrayList<String>());

	// Las demás pestañas
	private PalabrasController pController;
	
	public RootController() {

		// Antes cargar el fichero que contiene las palabras.......
		try {
			oList.add("Hola muchacho");
			pController = new PalabrasController(oList);
			
		} catch (IOException e) {
		}
		
		view = new RootView(pController.getBorderRoot());
	}
	
	public RootView getRootView() {
		return view;
	}
	
	public ObservableList<String> getoList() {
		return oList;
	}
}
