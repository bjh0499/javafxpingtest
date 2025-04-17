package pcRoomList.modal;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import pcRoomList.util.*;

public class InputIp
{
	static StringProperty[] result;
	
	public static StringProperty[] display(StringProperty[] input, int index, ObservableList<StringProperty[]> ipList)
	{
		result = null;
		
		String[] startIp;
		String[] endIp;
		
		if (input != null && UtilForIpList.isLegalIp(input[0].get()) && UtilForIpList.isLegalIp(input[1].get()))
		{
			startIp = input[0].get().split("\\.");
			endIp = input[1].get().split("\\.");
		}
		else
		{
			startIp = null;
			endIp = null;
		}
		
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("PC�� " + (input != null && UtilForIpList.isLegalIp(input[0].get()) && UtilForIpList.isLegalIp(input[1].get()) ? "����" : "�߰�"));
		window.setMinWidth(300);
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);
		
		Label startIpLabel = new Label("���� IP: ");
		GridPane.setConstraints(startIpLabel, 0, 0);
		
		TextField[] startIpTextField = new TextField[4];
		TextField[] endIpTextField = new TextField[4];
		
		for (int i = 0; i < 4; i++)
		{
			startIpTextField[i] = new TextField(startIp != null ? startIp[i] : "");
			startIpTextField[i].setMaxWidth(50);
			GridPane.setConstraints(startIpTextField[i], i+1, 0);
			startIpTextField[i].setOnKeyPressed(e ->
			{
				if (e.getCode().equals(KeyCode.ENTER))
				{
					isLegalInputIp(startIpTextField, endIpTextField, index, ipList);
					
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
		}
		
		Label endIpLabel = new Label("�� IP: ");
		GridPane.setConstraints(endIpLabel, 0, 1);
		
		for (int i = 0; i < 4; i++)
		{
			endIpTextField[i] = new TextField(endIp != null ? endIp[i] : "");
			endIpTextField[i].setMaxWidth(50);
			GridPane.setConstraints(endIpTextField[i], i+1, 1);
			endIpTextField[i].setOnKeyPressed(e ->
			{
				if (e.getCode().equals(KeyCode.ENTER))
				{
					isLegalInputIp(startIpTextField, endIpTextField, index, ipList);
					
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
		}
		
		grid.getChildren().add(startIpLabel);
		
		for (TextField tf : startIpTextField)
		{
			grid.getChildren().add(tf);
		}
		
		grid.getChildren().add(endIpLabel);
		
		for (TextField tf : endIpTextField)
		{
			grid.getChildren().add(tf);
		}
		
		Button yesButton = new Button("Ȯ��");
		yesButton.setOnAction(e ->
		{
			isLegalInputIp(startIpTextField, endIpTextField, index, ipList);
			
			if (result != null)
			{
				window.close();
			}
		});
		yesButton.setOnKeyPressed(e ->
		{
			if (e.getCode().equals(KeyCode.ENTER))
			{
				isLegalInputIp(startIpTextField, endIpTextField, index, ipList);
				
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
		
		Button noButton = new Button("���");
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
	
	private static void isLegalInputIp (TextField[] startIpTextField, TextField[] endIpTextField, int index, ObservableList<StringProperty[]> ipList)
	{
		String[] temp = new String[2];
		
		for (int i = 0; i < temp.length; i++)
		{
			temp[i] = "";
		}
		
		for (TextField tf : startIpTextField)
		{
			temp[0] += tf.getText() + ".";
		}
		
		temp[0] = temp[0].substring(0, temp[0].length() - 1);
		
		for (TextField tf : endIpTextField)
		{
			temp[1] += tf.getText() + ".";
		}
		
		temp[1] = temp[1].substring(0, temp[1].length() - 1);
		
		if (!(UtilForIpList.isLegalIp(temp[0]) && UtilForIpList.isLegalIp(temp[1])))
		{
			AlertBox.display("����", "�ùٸ� IP ������ �ƴմϴ�.");
			temp = null;
		}
		else
		{
			try
			{
				if (!UtilForIpList.isLegalIpRange(temp[0], temp[1]))
				{
					AlertBox.display("����", "���� IP�� �� IP���� Ů�ϴ�.");
					temp = null;
				}
			}
			catch (Exception e1)
			{
				AlertBox.display("����", "IP ���� ��ȯ �� ������ �߻��߽��ϴ�.");
				temp = null;
			}
			
			if (temp != null)
			{
				boolean duplicatedFlag = false;
				
				for (int i = 0; i < ipList.size(); i++)
				{
					if (i != index)
					{
						try
						{
							duplicatedFlag = UtilForIpList.isDuplicatedIp(ipList.get(i)[0].get(), ipList.get(i)[1].get(), temp[0], temp[1]);
						}
						catch (Exception e2)
						{
							AlertBox.display("����", "IP �ߺ� Ȯ�� �� ������ �߻��߽��ϴ�.");
							duplicatedFlag = true;
							temp = null;
						}
					}
					
					if (duplicatedFlag)
					{
						break;
					}
				}
				
				if (duplicatedFlag && temp != null)
				{
					AlertBox.display("����", "�ߺ��� IP�� �����մϴ�.");
					temp = null;
				}
				else
				{
					result = new StringProperty[2];
					result[0] = new SimpleStringProperty(temp[0]);
					result[1] = new SimpleStringProperty(temp[1]);
				}
			}
		}
	}
}
