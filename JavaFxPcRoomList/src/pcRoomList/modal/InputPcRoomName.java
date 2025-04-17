package pcRoomList.modal;

import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import pcRoomList.obj.*;

public class InputPcRoomName
{
	static String result;
	
	public static String display(String input, int index, ObservableList<PcRoomForTable> pcRoomList)
	{
		result = null;
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("PC방 " + (input.equals("") ? "추가" : "수정"));
		window.setMinWidth(250);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		Label pcRoomNameLabel = new Label("PC방 이름: ");
		GridPane.setConstraints(pcRoomNameLabel, 0, 0);
		
		TextField pcRoomNameInput = new TextField(input);
		GridPane.setConstraints(pcRoomNameInput, 1, 0);
		pcRoomNameInput.setOnKeyPressed(e ->
		{
			if (e.getCode().equals(KeyCode.ENTER))
			{
				isLegalInputName(pcRoomNameInput, index, pcRoomList);
				
				if (result != null)
				{
					window.close();
				}
			}
			else if (e.getCode().equals(KeyCode.ESCAPE))
			{
				window.close();
			}
		});
		
		grid.getChildren().addAll(pcRoomNameLabel, pcRoomNameInput);
		
		Button yesButton = new Button("확인");
		yesButton.setOnAction(e ->
		{
			isLegalInputName(pcRoomNameInput, index, pcRoomList);
			
			if (result != null)
			{
				window.close();
			}
		});
		yesButton.setOnKeyPressed(e ->
		{
			if (e.getCode().equals(KeyCode.ENTER))
			{
				isLegalInputName(pcRoomNameInput, index, pcRoomList);
				
				if (result != null)
				{
					window.close();
				}
			}
			else if (e.getCode().equals(KeyCode.ESCAPE))
			{
				window.close();
			}
		});
		
		Button noButton = new Button("취소");
		noButton.setOnAction(e ->
		{
			window.close();
		});
		noButton.setOnKeyPressed(e ->
		{
			if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE)
			{
				window.close();
			}
		});
		
		HBox hBox = new HBox(10);
		hBox.getChildren().addAll(yesButton, noButton);
		hBox.setAlignment(Pos.CENTER);
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(grid, hBox);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setResizable(false);
		window.setScene(scene);
		window.showAndWait();
		
		return result;
	}
	
	private static void isLegalInputName(TextField pcRoomNameInput, int index, ObservableList<PcRoomForTable> pcRoomList)
	{
		result = pcRoomNameInput.getText();
		
		if (result == null || result.equals(""))
		{
			AlertBox.display("오류", "PC방 이름을 공백으로 할 수 없습니다.");
		}
		else
		{
			boolean duplicatedFlag = false;
			
			for (int i = 0; i < pcRoomList.size(); i++)
			{
				if (i != index)
				{
					if (pcRoomList.get(i).getName().equals(result))
					{
						duplicatedFlag = true;
						break;
					}
				}
			}
			
			if (duplicatedFlag)
			{
				AlertBox.display("오류", "중복된 PC방 이름이 존재합니다.");
				result = null;
			}
		}
	}
}
