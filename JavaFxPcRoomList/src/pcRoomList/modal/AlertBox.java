package pcRoomList.modal;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class AlertBox
{
	public static void display(String title, String message)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button closeButton = new Button("´Ý±â");
		closeButton.setOnAction(e -> window.close());
		closeButton.setOnKeyPressed(e ->
		{
			if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE)
			{
				window.close();
			}
		});
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setResizable(false);
		window.setScene(scene);
		window.showAndWait();
		
		return;
	}
}