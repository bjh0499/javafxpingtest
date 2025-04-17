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
			AlertBox.display("오류", "파일 생성 중 오류가 발생했습니다.");
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
			AlertBox.display("오류", "파일 쓰기 중 오류가 발생했습니다.");
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
					AlertBox.display("오류", "설정 파일 생성 중 문제가 발생했습니다.");
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
						AlertBox.display("오류", "설정 파일 읽기 중 문제가 발생했습니다.");
						return null;
					}
				}
				
				boolean isLegalConfigFile = true;
				
				if (maxThreads < 0 || maxThreads > 100)
				{
					AlertBox.display("오류", "최대 Thread 수가 부적절하므로 기본값인 50으로 설정합니다.");
					maxThreads = 50;
					isLegalConfigFile = false;
				}
				
				if (limitTime < 101)
				{
					AlertBox.display("오류", "Ping Test 대기 시간이 부적절하거나 너무 짧으므로 기본값인 1초로 설정합니다.");
					limitTime = 1000;
					isLegalConfigFile = false;
				}
				
				if (delay < 101)
				{
					AlertBox.display("오류", "Thread 대기 시간이 부적절하거나 너무 짧으므로 기본값인 1.1초로 설정합니다.");
					delay = 1100;
					isLegalConfigFile = false;
				}
				
				if (defaultHour < 0 || defaultHour > 23 || defaultMinute < 0 || defaultMinute > 59 || defaultSecond < 0 || defaultSecond > 59 || (defaultHour == 0 && defaultMinute == 0))
				{
					AlertBox.display("오류", "부적절한 검사 주기이므로 기본값인 30분으로 설정합니다.");
					defaultHour = 0;
					defaultMinute = 30;
					defaultSecond = 0;
					isLegalConfigFile = false;
				}
				
				c = new Config(maxThreads, limitTime, delay, defaultHour, defaultMinute, defaultSecond);
				
				if (!isLegalConfigFile)
				{
					AlertBox.display("오류", "설정 파일에 오류가 있으므로 설정 파일을 재생성합니다.");
					configFileWrite(f, c);
				}
			}
			catch (FileNotFoundException e)
			{
				AlertBox.display("오류", "설정 파일 찾기 중 문제가 발생했습니다.");
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
					AlertBox.display("오류", "설정 파일 닫기 중 문제가 발생했습니다.");
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
			AlertBox.display("오류", "IP 목록 파일이 존재하지 않습니다.");
			return null;
		}
		else if (f.length() == 0)
		{
			AlertBox.display("오류", "IP 목록 파일이 비어 있습니다.");
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
				AlertBox.display("오류", "IP 목록 파일을 읽는 중 오류가 발생했습니다.");
				return null;
			}
			else if (pcRoomList.size() == 0)
			{
				AlertBox.display("오류", "IP 목록 파일을 읽는 중 오류가 발생했습니다.");
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
							AlertBox.display("오류", "IP 형식에 맞지 않는 데이터가 존재합니다.");
							return null;
						}
						
						try
						{
							if (!UtilForIpList.isLegalIpRange(ip1[0], ip1[1]))
							{
								AlertBox.display("오류", "시작 IP가 끝 IP보다 큰 데이터가 존재합니다.");
								return null;
							}
						}
						catch (Exception e)
						{
							AlertBox.display("오류", "IP 범위 체크 중 오류가 발생했습니다.");
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
								AlertBox.display("오류", "IP 형식에 맞지 않는 데이터가 존재합니다.");
								return null;
							}
							
							try
							{
								if (!UtilForIpList.isLegalIpRange(ip2[0], ip2[1]))
								{
									AlertBox.display("오류", "시작 IP가 끝 IP보다 큰 데이터가 존재합니다.");
									return null;
								}
							}
							catch (Exception e)
							{
								AlertBox.display("오류", "IP 범위 체크 중 오류가 발생했습니다.");
								return null;
							}
						}
						
						try
						{
							if (UtilForIpList.isDuplicatedIp(ip1[0], ip1[1], ip2[0], ip2[1]))
							{
								AlertBox.display("오류", "중복된 IP 데이터가 존재합니다.");
								return null;
							}
						}
						catch (Exception e)
						{
							AlertBox.display("오류", "IP 중복 체크 중 오류가 발생했습니다.");
							return null;
						}
					}
				}
			}
			
			return pcRoomList;
		}
		catch (FileNotFoundException fnfe)
		{
			AlertBox.display("오류", "IP 목록 파일을 찾을 수 없습니다.");
			return null;
		}
		catch (IOException ie)
		{
			AlertBox.display("오류", "IP 목록 파일을 읽는 중 오류가 발생했습니다.");
			return null;
		}
		catch (JsonSyntaxException jse)
		{
			AlertBox.display("오류", "IP 목록 파일 내용이 손상됐습니다.");
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
				AlertBox.display("오류", "IP 목록 파일 읽기 스트림 닫기 중 오류가 발생했습니다.");
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
			AlertBox.display("오류", "설정 파일 생성 중 오류가 발생했습니다.");
			return false;
		}
	}
}
