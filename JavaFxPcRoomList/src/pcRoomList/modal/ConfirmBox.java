package pcRoomList.modal;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class ConfirmBox
{
	static boolean result;
	
	public static boolean display(String title, String message)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button yesButton = new Button("확인");
		yesButton.setOnAction(e ->
		{
			result = true;
			window.close();
		});
		
		Button noButton = new Button("취소");
		noButton.setOnAction(e ->
		{
			result = false;
			window.close();
		});
		
		HBox hBox = new HBox(10);
		hBox.getChildren().addAll(yesButton, noButton);
		hBox.setAlignment(Pos.CENTER);
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, hBox);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setResizable(false);
		window.setScene(scene);
		window.showAndWait();
		
		return result;
	}
}
