package pcRoomList;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import javafx.application.*;
import javafx.beans.property.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import pcRoomList.obj.*;
import pcRoomList.util.*;
import pcRoomList.modal.*;

// Based on https://github.com/buckyroberts/Source-Code-from-Tutorials/blob/master/JavaFX/020_tableViewAddingAndDeleting/Main.java
public class PcRoomListConfig extends Application
{
	// XXX: Windows ȯ�濡 ���缭 �ۼ������� �ٸ� OS������ �����ϰ� �Ϸ��� �� �κ��� ������ �ʿ�.
	private static final String FILE_NAME = System.getProperty("user.dir") + "\\ipList.json";
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		Stage window = primaryStage;
		window.setTitle("PC�� IP ��� ���� ���α׷�");
		
		TableColumn<PcRoomForTable, String> pcRoomNameColumn = new TableColumn<PcRoomForTable, String>("PC�� �̸�");
		pcRoomNameColumn.setMinWidth(300);
		pcRoomNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty()); // �ش� Object �迭�� �����ϴ� ������ �Է��ϸ� �� ������ ����
		// https://code.makery.ch/kr/library/javafx-tutorial/part2/
		
		TableColumn<PcRoomForTable, Long> pcCountColumn = new TableColumn<PcRoomForTable, Long>("PC ��");
		pcCountColumn.setMinWidth(100);
		pcCountColumn.setCellValueFactory(cellData -> cellData.getValue().getPcCountProperty().asObject());
		
		TableView<PcRoomForTable> table = new TableView<PcRoomForTable>();
		ObservableList<PcRoomForTable> products = FXCollections.observableArrayList();
		
		if (!pcRoomListInitialize(products))
		{
			return;
		}
		
		table.setItems(products);
		table.getColumns().addAll(pcRoomNameColumn, pcCountColumn);
		
		ObservableList<PcRoomForTable> items = table.getItems();
		
		Button addButton = new Button("PC�� �߰�");
		addButton.setOnAction(e ->
		{
			String input = InputPcRoomName.display("", -1, items);
			
			if (input != null && !input.equals(""))
			{
				items.add(new PcRoomForTable(input, new ArrayList<String[]>()));
			}
		});
		
		Button modifyButton = new Button("PC�� �̸� ����");
		modifyButton.setOnAction(e ->
		{
			PcRoomForTable p = table.getSelectionModel().getSelectedItems().get(0);
			
			if (p != null)
			{
				int index = items.indexOf(p);
				String input = InputPcRoomName.display(p.getName(), index, items);
				
				if (input != null && !input.equals(""))
				{
					items.get(index).setName(input);
				}
			}
			else
			{
				AlertBox.display("����", "�����ϰ��� �ϴ� PC���� �����ؾ� �մϴ�.");
			}
		});
		
		Button ipConfigButton = new Button("IP ����");
		ipConfigButton.setOnAction(e ->
		{
			PcRoomForTable p = table.getSelectionModel().getSelectedItems().get(0);
			
			if (p != null)
			{
				ArrayList<StringProperty[]> ipList = IpList.display(p.getName(), p.getIpList());
				
				if (ipList != null)
				{
					p.refreshIpList(ipList);
				}
			}
			else
			{
				AlertBox.display("����", "�����ϰ��� �ϴ� PC���� �����ؾ� �մϴ�.");
			}
		});
		
		Button deleteButton = new Button("PC�� ����");
		deleteButton.setOnAction(e ->
		{
			if (ConfirmBox.display("PC�� ����", "������ PC���� �����մϴ�."))
			{
				items.removeAll(table.getSelectionModel().getSelectedItems());
			}
		});
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setSpacing(28);
		hBox.getChildren().addAll(addButton, modifyButton, ipConfigButton, deleteButton);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(table, hBox);
		
		Scene scene = new Scene(vBox);
		window.setResizable(false);
		window.setScene(scene);
		window.show();
		
		window.setOnCloseRequest(e ->
		{
			e.consume();
			closeProgram(window, items);
		});
		
		return;
	}
	
	private boolean pcRoomListInitialize(ObservableList<PcRoomForTable> products)
	{
		File f = null;
		FileInputStream fis = null;
		ArrayList<PcRoom> pcRoomList = null;
		Gson gson = new Gson();
		
		try
		{
			f = new File(FILE_NAME);
			if (!f.exists())
			{
				f.createNewFile();
				return true;
			}
			else if (f.length() == 0)
			{
				return true;
			}
		}
		catch (IOException ie)
		{
			AlertBox.display("����", "IP ��� ���� ���� �� ������ �߻��߽��ϴ�.");
			return false;
		}
		
		try
		{
			fis = new FileInputStream(FILE_NAME);
			byte[] readBuffer = new byte[fis.available()];
			
			while (fis.read(readBuffer) != -1)
            {
				;
            }
			
			Type collectionType = new TypeToken<Collection<PcRoom>>(){}.getType();
			pcRoomList = gson.fromJson(new String(readBuffer), collectionType);
			
			for (PcRoom p : pcRoomList)
			{
				products.add(new PcRoomForTable(p));
			}
			
			return true;
		}
		catch (FileNotFoundException fnfe)
		{
			AlertBox.display("����", "IP ��� ������ ã�� �� �����ϴ�.");
			return false;
		}
		catch (IOException ie)
		{
			AlertBox.display("����", "IP ��� ������ �д� �� ������ �߻��߽��ϴ�.");
			return false;
		}
		catch (JsonSyntaxException jse)
		{
			AlertBox.display("����", "IP ��� ���� ������ �ջ�ƽ��ϴ�.");
			return false;
		}
		finally
		{
			try
			{
				if (fis != null)
				{
					fis.close();
				}
			}
			catch (IOException ie)
			{
				AlertBox.display("����", "IP ��� ���� �б� ��Ʈ�� �ݱ� �� ������ �߻��߽��ϴ�.");
			}
		}
	}
	
	private void closeProgram(Stage window, ObservableList<PcRoomForTable> items)
	{
		if (ConfirmBox.display("���α׷� ����", "���α׷��� �����մϱ�?"))
		{
			save(items);
			window.close();
		}
		
		return;
	}
	
	private void save(ObservableList<PcRoomForTable> items)
	{
		ArrayList<PcRoom> pcRoomList = new ArrayList<PcRoom>();
		
		for (PcRoomForTable temp : items)
		{
			pcRoomList.add(new PcRoom(temp.getName(), UtilForPcRoomList.convertIpList(temp.getIpList())));
		}
		
		File file = null;
		FileOutputStream out = null;
		Gson gson = new Gson();
		String pcRoomJson = gson.toJson(pcRoomList);
		
		try
		{
			file = new File(FILE_NAME);
			
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			out = new FileOutputStream(FILE_NAME);
			byte[] pcRoomJsonByte = pcRoomJson.getBytes();
			out.write(pcRoomJsonByte);
		}
		catch (FileNotFoundException fnfe)
		{
			AlertBox.display("����", "IP ��� ������ ã�� �� �����ϴ�.");
		}
		catch (IOException ie)
		{
			AlertBox.display("����", "IP ��� ���� ���� �� ������ �߻��߽��ϴ�.");
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (IOException ie)
			{
				AlertBox.display("����", "IP ��� ���� ���� ��Ʈ�� �ݱ� �� ������ �߻��߽��ϴ�.");
			}
		}
		
		return;
	}
	
	public static void main(String[] args)
	{
		launch(args);
		
		return;
	}
}