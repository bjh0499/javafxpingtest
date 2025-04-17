package pingTest;

import java.io.*;
import java.time.format.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import javafx.animation.*;
import javafx.application.*;
import javafx.concurrent.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import javafx.util.*;

import pingTest.modal.*;
import pingTest.obj.*;
import pingTest.util.*;

//Based on https://gist.github.com/jewelsea/3388637
//2019/12/28~2020/01/07
public class PingTestScheduler extends Application
{
	// XXX: Windows 환경에 맞춰서 작성했으나 다른 OS에서도 동작하게 하려면 이 부분의 수정이 필요.
	private static final String IP_LIST_JSON_FILE_NAME = System.getProperty("user.dir") + "\\ipList.json";
	private static final String CONFIG_FILE_NAME = System.getProperty("user.dir") + "\\config.cfg";
	private static final String OUTPUT_FILE_NAME = System.getProperty("user.dir") + "\\result.txt";
	
	/*
	private static final String IP_LIST_JSON_FILE_NAME = "E:\\ipList2.json";
	private static final String CONFIG_FILE_NAME = "E:\\config.cfg";
	private static final String OUTPUT_FILE_NAME = "E:\\result.txt";
	*/
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 a hh시 mm분");
	private ArrayList<PcRoom> pcRoomList;
	private PingTest pt;
	
	private Stage window;
	private Timer timer;
	private Button runButton;
	private TextArea textArea;
	
	private File configFile;
	private File outputFile;
	private Config c;
	
	private Task<Void> task;
	private Thread thread;
	
	private class Timer extends Label
	{
		private AtomicInteger defaultHour;
		private AtomicInteger defaultMinute;
		private AtomicInteger defaultSecond;
		
		private AtomicInteger hour;
		private AtomicInteger minute;
		private AtomicInteger second;
		private AtomicInteger milliSecond;
		
		
		Timeline timeline;
		
		Timer(int inputHour, int inputMinute, int inputSecond)
		{
			modifyTime(inputHour, inputMinute, inputSecond);
			task = null;
			thread = null;
		}
		
		public void start()
		{
			timeline = new Timeline(
				new KeyFrame(Duration.ZERO, new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent actionEvent)
					{
						if (hour.get() == 0 && minute.get() == 0 && second.get() == 0)
						{
							if (task == null)
							{
								runButton.setTextFill(Color.RED);
								runButton.setText("검사 중");
								
								if (textArea.getLength() > 50000)
								{
									textArea.clear();
								}
								
								task = new Task<Void>()
								{
								    @Override
								    protected Void call() throws Exception
								    {
								    	for (PcRoom p : pcRoomList)
								    	{
								    		PingTestResult ptr = pt.start(p);
								    		String s = "[" + ptr.getStartTime().format(dtf) + "] " + ptr.getPcRoomName() + " - 가동 PC 수: " + ptr.getTurnOnPcCount() + "/" + ptr.getTotalPcCount() + "(" + (((ptr.getTurnOnPcCount() * 100) / ptr.getTotalPcCount()) + ((ptr.getTurnOnPcCount() * 100) % ptr.getTotalPcCount() == 0 ? 0 : 1)) + "%)" + "\r\n";
								    		
								    		Platform.runLater( () -> 
									    	{
									    		textArea.appendText(s);
									    	});
								    		
								    		UtilForFile.fileWriteString(outputFile, s);
								    	}
								    	
								    	String line = "========================================\r\n";
								    	
								    	Platform.runLater( () -> 
								    	{
								    		textArea.appendText("\r\n" + line + "\r\n");
								    	});
								    	
								    	UtilForFile.fileWriteString(outputFile, line);
								    	
								    	return null;
								    }
								};
								
								thread = new Thread(task);
								
								thread.start();
							}
							else if (thread.isAlive() && !window.isShowing())
							{
								thread.interrupt();
							}
							else if (!thread.isAlive())
							{
								resetTimer();
								
								runButton.setTextFill(Color.BLACK);
								runButton.setText("바로 실행");
								
								thread = null;
								task = null;
							}
						}
						else
						{
							milliSecond.getAndDecrement();
							if (milliSecond.compareAndSet(-1, 9))
							{
								second.getAndDecrement();
								
								if (second.compareAndSet(-1, 59))
								{
									minute.getAndDecrement();
									
									if (minute.compareAndSet(-1, 59))
									{
										hour.getAndDecrement();
									}
								}
							}
						}
						
						String hourString = pad(2, '0', hour.get() + "");
						String minuteString = pad(2, '0', minute.get() + "");
						String secondString = pad(2, '0', second.get() + "");
						
						setText(hourString + ":" + minuteString + ":" + secondString);
					}}),
				new KeyFrame(new Duration(100)));
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
			
			return;
		}
		
		public void modifyTime(int inputHour, int inputMinute, int inputSecond)
		{
			if (defaultHour == null)
			{
				defaultHour = new AtomicInteger(inputHour);
				defaultMinute = new AtomicInteger(inputMinute);
				defaultSecond = new AtomicInteger(inputSecond);
				
				hour = new AtomicInteger(defaultHour.get());
				minute = new AtomicInteger(defaultMinute.get());
				second = new AtomicInteger(defaultSecond.get());
				milliSecond = new AtomicInteger(10);
			}
			else
			{
				timeline.stop();
				
				defaultHour.set(inputHour);
				defaultMinute.set(inputMinute);
				defaultSecond.set(inputSecond);
				
				hour.set(inputHour);
				minute.set(inputMinute);
				second.set(inputSecond);
				milliSecond.set(10);
				
				timeline.play();
			}
		}
		
		public boolean isThreadOn()
		{
			return thread != null && thread.isAlive();
		}
		
		public void setToZero()
		{
			hour.set(0);
			minute.set(0);
			second.set(0);
			milliSecond.set(10);
		}
		
		private void resetTimer()
		{
			hour.set(defaultHour.get());
			minute.set(defaultMinute.get());
			second.set(defaultSecond.get());
			milliSecond.set(10);
		}
		
		/**
		 * Creates a string left padded to the specified width with the supplied padding
		 * character.
		 * 
		 * @param fieldWidth the length of the resultant padded string.
		 * @param padChar    a character to use for padding the string.
		 * @param s          the string to be padded.
		 * @return the padded string.
		 */
		private String pad(int fieldWidth, char padChar, String s)
		{
			StringBuilder sb = new StringBuilder();
			for (int i = s.length(); i < fieldWidth; i++)
			{
				sb.append(padChar);
			}
			sb.append(s);

			return sb.toString();
		}
	}
	
	private boolean pingTestConfig()
	{
		pcRoomList = UtilForFile.pcRoomListLoad(IP_LIST_JSON_FILE_NAME);
		
		if (pcRoomList == null)
		{
			return false;
		}
		
		configFile = UtilForFile.fileRead(CONFIG_FILE_NAME);
		outputFile = UtilForFile.fileRead(OUTPUT_FILE_NAME);
		
		if (configFile == null || outputFile == null)
		{
			return false;
		}
		
		c = UtilForFile.configFileLoader(configFile);
		
		if (c == null || c.getDelay() < 0 || c.getLimitTime() < 0 || c.getMaxThreads() < 0)
		{
			return false;
		}
		else
		{
			timer = new Timer(c.getDefaultHour(), c.getDefaultMinute(), c.getDefaultSecond());
			// timer = new Timer(0, 0, 1);
			pt = new PingTest(c);
		}
		
		return true;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		window = primaryStage;
		
		if (!pingTestConfig())
		{
			return;
		}
		
		GridPane gridPane = new GridPane();
		
		HBox hBox1 = new HBox();
		hBox1.setMinWidth(220);
		hBox1.setAlignment(Pos.CENTER_LEFT);
		hBox1.setPadding(new Insets(10, 10, 10, 10));
		hBox1.getChildren().addAll(new Label("다음 검사 시간까지 "), timer);
		
		HBox hBox2 = new HBox();
		hBox2.setMinWidth(120);
		hBox2.setAlignment(Pos.CENTER);
		hBox2.setPadding(new Insets(10, 10, 10, 10));
		
		runButton = new Button("바로 실행");
		runButton.setOnAction(e -> 
		{
			timer.setToZero();
		});
		
		hBox2.getChildren().add(runButton);
		
		TextField hourTextField = new TextField();
		TextField minuteTextField = new TextField();
		TextField secondTextField = new TextField();
		
		hourTextField.setMaxWidth(40);
		minuteTextField.setMaxWidth(40);
		secondTextField.setMaxWidth(40);
		
		Button modifyButton = new Button("변경");
		modifyButton.setOnAction(e -> 
		{
			try
			{
				if (!timer.isThreadOn())
				{
					int inputHour = Integer.parseInt(hourTextField.getText());
					int inputMinute = Integer.parseInt(minuteTextField.getText());
					int inputSecond = Integer.parseInt(secondTextField.getText());
					
					if (inputHour < 0 || inputHour > 23 || inputMinute < 0 || inputMinute > 59 || inputSecond < 0 || inputSecond > 59)
					{
						AlertBox.display("오류", "시간 란에 잘못된 숫자 값이 들어 있습니다.");
						return;
					}
					else if (inputHour == 0 && inputMinute == 0)
					{
						AlertBox.display("오류", "검사 주기가 지나치게 짧습니다.");
						return;
					}
					
					timer.modifyTime(inputHour, inputMinute, inputSecond);
					c.setDefaultHour(inputHour);
					c.setDefaultMinute(inputMinute);
					c.setDefaultSecond(inputSecond);
				}
			}
			catch (NumberFormatException nfe)
			{
				AlertBox.display("오류", "시간 란에 숫자가 아닌 값이 들어 있습니다.");
			}
			
			return;
		});
		
		HBox hBox3 = new HBox();
		hBox3.setAlignment(Pos.CENTER_RIGHT);
		hBox3.setMinWidth(260);
		hBox3.setPadding(new Insets(10, 10, 10, 10));
		hBox3.getChildren().addAll(new Label("검사 주기"), hourTextField, new Label(":"), minuteTextField, new Label(":"), secondTextField, modifyButton);
		
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setMinHeight(340);
		textArea.setMaxWidth(590);
		
		gridPane.add(hBox1, 0, 0, 1, 1);
		gridPane.add(hBox2, 1, 0, 1, 1);
		gridPane.add(hBox3, 2, 0, 1, 1);
		gridPane.add(textArea, 0, 1, 3, 1);
		
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.setMinWidth(580);
		gridPane.setMinHeight(380);
		
		Scene scene = new Scene(gridPane, 600, 400);
		
		timer.start();
		
		window.setTitle("핑 테스트 프로그램");
		window.setResizable(false);
		window.setScene(scene);
		window.show();
		
		window.setOnCloseRequest(e ->
		{
			e.consume();
			if (ConfirmBox.display("프로그램 종료", "프로그램을 종료합니까?"))
			{
				UtilForFile.configFileSave(configFile, c);
				window.close();
			}
		});
	}

	public static void main(String args[])
	{
		launch(args);
	}
}
