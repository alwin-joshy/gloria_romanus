package unsw.gloriaromanus;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class ToggleSwitch extends HBox {
	// Added this
	
	private final Label label = new Label();
	private final Button button = new Button();
	private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
	public SimpleBooleanProperty selectedProperty() { return selected; }
	public boolean isSelected() {return selected.getValue();}
	
	private void init() {
		label.setText("OFF");
		getChildren().addAll(label, button);	
		button.setOnAction((e) -> {
			selected.set(!selected.get());
		});
		label.setOnMouseClicked((e) -> {
			selected.set(!selected.get());
		});
		setStyle();
		bindProperties();
	}
	
	private void setStyle() {
		//Default Width
		setWidth(80);
		label.setAlignment(Pos.CENTER);
		setStyle("-fx-text-fill:black; -fx-background-radius: 10;");
		setStyle("-fx-background-color: #2E4053;");
		setAlignment(Pos.CENTER_LEFT);
	}
	
	private void bindProperties() {
		label.prefWidthProperty().bind(widthProperty().divide(2));
		label.prefHeightProperty().bind(heightProperty());
		button.prefWidthProperty().bind(widthProperty().divide(2));
		button.prefHeightProperty().bind(heightProperty());
	}
	
	public ToggleSwitch() {
		init();
		selected.addListener((a,b,c) -> {
			if (c) {
                		label.setText("ON");
						setStyle("-fx-background-color: #5D6D7E;");
                		label.toFront();
            		}
            		else {
            			label.setText("OFF");
						setStyle("-fx-background-color: #2E4053;");
                		button.toFront();
            		}
		});
	}
}