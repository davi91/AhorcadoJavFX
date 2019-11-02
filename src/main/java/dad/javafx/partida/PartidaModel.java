package dad.javafx.partida;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class PartidaModel {

	private IntegerProperty puntos = new SimpleIntegerProperty();
	private IntegerProperty vidas = new SimpleIntegerProperty();
	
	/**
	 * La frase o palabra a adivinar
	 */
	private StringProperty adivinar = new SimpleStringProperty(); 
	/**
	 * Las letras usadas
	 */
	private StringProperty usado = new SimpleStringProperty("");
	
	/**
	 * Palabra o frase a intentar adivinar
	 */
	private StringProperty intento = new SimpleStringProperty();
	
	/**
	 * Imagen del ahorcado
	 */
	private ObjectProperty<Image> image= new SimpleObjectProperty<>();


	public final StringProperty adivinarProperty() {
		return this.adivinar;
	}
	

	public final String getAdivinar() {
		return this.adivinarProperty().get();
	}
	

	public final void setAdivinar(final String adivinar) {
		this.adivinarProperty().set(adivinar);
	}
	

	public final StringProperty usadoProperty() {
		return this.usado;
	}
	

	public final String getUsado() {
		return this.usadoProperty().get();
	}
	

	public final void setUsado(final String usado) {
		this.usadoProperty().set(usado);
	}
	

	public final StringProperty intentoProperty() {
		return this.intento;
	}
	

	public final String getIntento() {
		return this.intentoProperty().get();
	}
	

	public final void setIntento(final String intento) {
		this.intentoProperty().set(intento);
	}
	

	public final ObjectProperty<Image> imageProperty() {
		return this.image;
	}
	

	public final Image getImage() {
		return this.imageProperty().get();
	}
	

	public final void setImage(final Image image) {
		this.imageProperty().set(image);
	}


	public final IntegerProperty puntosProperty() {
		return this.puntos;
	}
	


	public final int getPuntos() {
		return this.puntosProperty().get();
	}
	


	public final void setPuntos(final int puntos) {
		this.puntosProperty().set(puntos);
	}
	


	public final IntegerProperty vidasProperty() {
		return this.vidas;
	}
	


	public final int getVidas() {
		return this.vidasProperty().get();
	}
	


	public final void setVidas(final int vidas) {
		this.vidasProperty().set(vidas);
	}
	
	
	
	
}
