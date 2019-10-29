package dad.javafx.ahorcado;

import java.io.IOException;

import dad.javafx.palabras.PalabrasController;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class RootView extends TabPane {

	public RootView(Node pNode) {
		
		 Tab tab1 = new Tab("Partida");
		 
		 Tab tab2 = new Tab("Palabras");
		 
		 Tab tab3 = new Tab("Puntuaciones");
		 
		 tab2.setContent(pNode);
		 
		 // No cerramos las pestañas
		 setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		 getTabs().addAll(tab1, tab2, tab3);
	}
}
