package pingTest.util;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.*;

import pingTest.modal.*;
import pingTest.obj.*;

public class UtilForFile
{
	public static File fileRead(String FILE_NAME)
	{
		File f = null;
		
		try
		{
			f = new File(FILE_NAME);
			if (!f.exists())
			{
				f.createNewFile();
			}
		}
		catch (IOException ie)
		{
			AlertBox.display("����", "���� ���� �� ������ �߻��߽��ϴ�.");
			return null;
		}
		
		return f;
	}
	
	public static void fileWriteString(File f, String s)
	{
		try
		{
			FileWriter fw = new FileWriter (f, true);
			fw.write(s);
			fw.close();
		}
		catch (IOException e)
		{
			AlertBox.display("����", "���� ���� �� ������ �߻��߽��ϴ�.");
		}
		
		return;
	}
	
	public static Config configFileLoader(File f)
	{
		Config c = null;
		
		if (!f.exists() || f.length() == 0)
		{
			if (!f.exists())
			{
				try
				{
					f.createNewFile();
				}
				catch (IOException e)
				{
					AlertBox.display("����", "���� ���� ���� �� ������ �߻��߽��ϴ�.");
					return null;
				}
			}
			
			c = defaultConfig();
			configFileWrite(f, c);
		}
		else
		{
			BufferedReader br = null;
			
			try
			{
				br = new BufferedReader(new FileReader(f));
				
				int maxThreads = -1;
				int limitTime = -1;
				int delay = -1;
				int defaultHour = -1;
				int defaultMinute = -1;
				int defaultSecond = -1;
				
				while (true)
				{
					try
					{
						String line = br.readLine();
						
						if (line == null)
						{
							break;
						}
						
						String[] strArr = line.split("\\s");
						
						if (strArr.length == 2)
						{
							switch(strArr[0])
							{
								case "MAX_THREADS:":
									maxThreads = Integer.parseInt(strArr[1]);
									break;
								case "LIMIT_TIME:":
									limitTime = Integer.parseInt(strArr[1]);
									break;
								case "DELAY:":
									delay = Integer.parseInt(strArr[1]);
									break;
								case "DEFAULT_HOUR:":
									defaultHour = Integer.parseInt(strArr[1]);
									break;
								case "DEFAULT_MINUTE:":
									defaultMinute = Integer.parseInt(strArr[1]);
									break;
								case "DEFAULT_SECOND:":
									defaultSecond = Integer.parseInt(strArr[1]);
									break;
								default:
							}
						}
					}
					catch (NumberFormatException nfe)
					{
						break;
					}
					catch (IOException e)
					{
						AlertBox.display("����", "���� ���� �б� �� ������ �߻��߽��ϴ�.");
						return null;
					}
				}
				
				boolean isLegalConfigFile = true;
				
				if (maxThreads < 0 || maxThreads > 100)
				{
					AlertBox.display("����", "�ִ� Thread ���� �������ϹǷ� �⺻���� 50���� �����մϴ�.");
					maxThreads = 50;
					isLegalConfigFile = false;
				}
				
				if (limitTime < 101)
				{
					AlertBox.display("����", "Ping Test ��� �ð��� �������ϰų� �ʹ� ª���Ƿ� �⺻���� 1�ʷ� �����մϴ�.");
					limitTime = 1000;
					isLegalConfigFile = false;
				}
				
				if (delay < 101)
				{
					AlertBox.display("����", "Thread ��� �ð��� �������ϰų� �ʹ� ª���Ƿ� �⺻���� 1.1�ʷ� �����մϴ�.");
					delay = 1100;
					isLegalConfigFile = false;
				}
				
				if (defaultHour < 0 || defaultHour > 23 || defaultMinute < 0 || defaultMinute > 59 || defaultSecond < 0 || defaultSecond > 59 || (defaultHour == 0 && defaultMinute == 0))
				{
					AlertBox.display("����", "�������� �˻� �ֱ��̹Ƿ� �⺻���� 30������ �����մϴ�.");
					defaultHour = 0;
					defaultMinute = 30;
					defaultSecond = 0;
					isLegalConfigFile = false;
				}
				
				c = new Config(maxThreads, limitTime, delay, defaultHour, defaultMinute, defaultSecond);
				
				if (!isLegalConfigFile)
				{
					AlertBox.display("����", "���� ���Ͽ� ������ �����Ƿ� ���� ������ ������մϴ�.");
					configFileWrite(f, c);
				}
			}
			catch (FileNotFoundException e)
			{
				AlertBox.display("����", "���� ���� ã�� �� ������ �߻��߽��ϴ�.");
				return null;
			}
			finally
			{
				try
				{
					if (br != null)
					{
						br.close();
					}
					
				}
				catch (IOException e)
				{
					AlertBox.display("����", "���� ���� �ݱ� �� ������ �߻��߽��ϴ�.");
				}
			}
		}
		
		return c;
	}
	
	public static void configFileSave(File f, Config c)
	{
		configFileWrite(f, c);
		
		return;
	}
	
	public static ArrayList<PcRoom> pcRoomListLoad(final String FILE_NAME)
	{
		File f = null;
		FileInputStream fis = null;
		ArrayList<PcRoom> pcRoomList;
		Gson gson = new Gson();
		
		f = new File(FILE_NAME);
		if (!f.exists())
		{
			AlertBox.display("����", "IP ��� ������ �������� �ʽ��ϴ�.");
			return null;
		}
		else if (f.length() == 0)
		{
			AlertBox.display("����", "IP ��� ������ ��� �ֽ��ϴ�.");
			return null;
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
			
			if (pcRoomList == null)
			{
				AlertBox.display("����", "IP ��� ������ �д� �� ������ �߻��߽��ϴ�.");
				return null;
			}
			else if (pcRoomList.size() == 0)
			{
				AlertBox.display("����", "IP ��� ������ �д� �� ������ �߻��߽��ϴ�.");
				return null;
			}
			
			for (PcRoom p : pcRoomList)
			{
				for (int i = 0; i < p.getIpList().size() - 1; i++)
				{
					String[] ip1 = p.getIpList().get(i);
					
					if (i == 0)
					{
						if (!(UtilForIpList.isLegalIp(ip1[0]) && UtilForIpList.isLegalIp(ip1[1])))
						{
							AlertBox.display("����", "IP ���Ŀ� ���� �ʴ� �����Ͱ� �����մϴ�.");
							return null;
						}
						
						try
						{
							if (!UtilForIpList.isLegalIpRange(ip1[0], ip1[1]))
							{
								AlertBox.display("����", "���� IP�� �� IP���� ū �����Ͱ� �����մϴ�.");
								return null;
							}
						}
						catch (Exception e)
						{
							AlertBox.display("����", "IP ���� üũ �� ������ �߻��߽��ϴ�.");
							return null;
						}
					}
					
					for (int j = i + 1; j < p.getIpList().size(); j++)
					{
						String[] ip2 = p.getIpList().get(j);
						if (i == 0)
						{
							if (!(UtilForIpList.isLegalIp(ip2[0]) && UtilForIpList.isLegalIp(ip2[1])))
							{
								AlertBox.display("����", "IP ���Ŀ� ���� �ʴ� �����Ͱ� �����մϴ�.");
								return null;
							}
							
							try
							{
								if (!UtilForIpList.isLegalIpRange(ip2[0], ip2[1]))
								{
									AlertBox.display("����", "���� IP�� �� IP���� ū �����Ͱ� �����մϴ�.");
									return null;
								}
							}
							catch (Exception e)
							{
								AlertBox.display("����", "IP ���� üũ �� ������ �߻��߽��ϴ�.");
								return null;
							}
						}
						
						try
						{
							if (UtilForIpList.isDuplicatedIp(ip1[0], ip1[1], ip2[0], ip2[1]))
							{
								AlertBox.display("����", "�ߺ��� IP �����Ͱ� �����մϴ�.");
								return null;
							}
						}
						catch (Exception e)
						{
							AlertBox.display("����", "IP �ߺ� üũ �� ������ �߻��߽��ϴ�.");
							return null;
						}
					}
				}
			}
			
			return pcRoomList;
		}
		catch (FileNotFoundException fnfe)
		{
			AlertBox.display("����", "IP ��� ������ ã�� �� �����ϴ�.");
			return null;
		}
		catch (IOException ie)
		{
			AlertBox.display("����", "IP ��� ������ �д� �� ������ �߻��߽��ϴ�.");
			return null;
		}
		catch (JsonSyntaxException jse)
		{
			AlertBox.display("����", "IP ��� ���� ������ �ջ�ƽ��ϴ�.");
			return null;
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
	
	private static Config defaultConfig()
	{
		return new Config(50, 1000, 1100, 0, 30, 0);
	}
	
	private static boolean configFileWrite(File f, Config c)
	{
		try
		{
			f.delete();
			f.createNewFile();
			
			fileWriteString(f, "MAX_THREADS: " + c.getMaxThreads() + "\r\n");
			fileWriteString(f, "LIMIT_TIME: " + c.getLimitTime() + "\r\n");
			fileWriteString(f, "DELAY: " + c.getDelay() + "\r\n");
			fileWriteString(f, "DEFAULT_HOUR: " + c.getDefaultHour() + "\r\n");
			fileWriteString(f, "DEFAULT_MINUTE: " + c.getDefaultMinute() + "\r\n");
			fileWriteString(f, "DEFAULT_SECOND: " + c.getDefaultSecond() + "\r\n");
			
			return true;
		}
		catch (IOException e)
		{
			AlertBox.display("����", "���� ���� ���� �� ������ �߻��߽��ϴ�.");
			return false;
		}
	}
}
