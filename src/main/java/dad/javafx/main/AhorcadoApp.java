package dad.javafx.main;

import dad.javafx.ahorcado.RootController;
import javafx.application.Application;
import javafx.scene.Scene;
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

	public static void main(String[] args) {
		launch(args);

	}

}
