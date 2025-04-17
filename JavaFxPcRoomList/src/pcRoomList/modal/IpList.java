package pcRoomList.modal;

import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import pcRoomList.util.*;

public class IpList
{
	static ArrayList<StringProperty[]> result;
	
	public static ArrayList<StringProperty[]> display(String PcRoomName, ArrayList<StringProperty[]> ipList)
	{
		result = null;
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("IP 수정 (" + PcRoomName + ")");
		window.setMinWidth(250);
		
		TableColumn<StringProperty[], String> startIpColumn = new TableColumn<StringProperty[], String>("시작 IP");
		startIpColumn.setMinWidth(150);
		startIpColumn.setCellValueFactory(cellData -> cellData.getValue()[0]);
		
		TableColumn<StringProperty[], String> endIpColumn = new TableColumn<StringProperty[], String>("끝 IP");
		endIpColumn.setMinWidth(150);
		endIpColumn.setCellValueFactory(cellData -> cellData.getValue()[1]);
		
		TableView<StringProperty[]> table = new TableView<StringProperty[]>();
		ObservableList<StringProperty[]> products = FXCollections.observableArrayList();
		
		for (StringProperty[] ip : ipList)
		{
			products.add(ip);
		}
		
		table.setItems(products);
		table.getColumns().addAll(startIpColumn, endIpColumn);
		
		ObservableList<StringProperty[]> items = table.getItems();
		
		Button addButton = new Button("IP 추가");
		addButton.setOnAction(e ->
		{
			StringProperty[] input = InputIp.display(null, -1, items);
			
			if (input != null)
			{
				items.add(input);
				UtilForIpList.ipListSort(items);
			}
		});
		
		Button modifyButton = new Button("IP 수정");
		modifyButton.setOnAction(e ->
		{
			StringProperty[] ip = table.getSelectionModel().getSelectedItems().get(0);
			
			if (ip != null)
			{
				int index = items.indexOf(ip);
				StringProperty[] input = InputIp.display(ip, index, items);
				
				if (input != null)
				{
					items.get(index)[0].set(input[0].get());
					items.get(index)[1].set(input[1].get());
					UtilForIpList.ipListSort(items);
				}
			}
			else
			{
				AlertBox.display("오류", "변경하고자 하는 IP를 선택해야 합니다.");
			}
		});
		
		Button deleteButton = new Button("IP 삭제");
		deleteButton.setOnAction(e ->
		{
			if (ConfirmBox.display("IP 삭제", "선택한 IP를 삭제합니다."))
			{
				items.removeAll(table.getSelectionModel().getSelectedItems());
			}
		});
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(5, 5, 5, 5));
		hBox.setSpacing(65);
		hBox.getChildren().addAll(addButton, modifyButton, deleteButton);
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(table, hBox);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setResizable(false);
		window.setScene(scene);
		window.setOnCloseRequest(e ->
		{
			e.consume();
			
			result = new ArrayList<StringProperty[]>();
			for (StringProperty[] ip : items)
			{
				result.add(ip);
			}
			
			window.close();
		});
		
		window.showAndWait();
		
		return result;
	}
}