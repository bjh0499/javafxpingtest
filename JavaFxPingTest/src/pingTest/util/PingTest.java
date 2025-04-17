package pingTest.util;

import java.io.*;
import java.net.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import pingTest.modal.AlertBox;
import pingTest.obj.*;

//Based on https://www.geeksforgeeks.org/pinging-ip-address-java/
public class PingTest
{
	final int MAX_THREADS;
	final int LIMIT_TIME;
	final int DELAY;
	private AtomicInteger threadsCounter;
	private AtomicLong atomicTurnOnPcCount;
	private AtomicLong remain;
	
	public PingTest(Config c)
	{
		MAX_THREADS = c.getMaxThreads();
		LIMIT_TIME = c.getLimitTime();
		DELAY = c.getDelay();
	}
	
	private class PingTestThread extends Thread
	{
		String ip;

		public PingTestThread(String ip)
		{
			this.ip = ip;
		}

		public void run()
		{
			threadsCounter.getAndIncrement();
			try
			{
				sendPingRequest(ip);
			}
			catch (Exception e)
			{
				AlertBox.display("오류", ip + ": 오류가 발생했습니다.");
			}
			finally
			{
				threadsCounter.getAndDecrement();
			}
			
			return;
		}
	}
	
	// Sends ping request to a provided IP address
	private void sendPingRequest(String ip) throws UnknownHostException, IOException
	{
		try
		{
			InetAddress ping = InetAddress.getByName(ip);
			
			if (ping.isReachable(LIMIT_TIME))
			{
				atomicTurnOnPcCount.getAndIncrement();
			}
			else
			{
				;
			}
			
			remain.getAndDecrement();
		}
		catch (UnknownHostException uhe)
		{
			AlertBox.display("오류", ip + ": UnknownHostException");
		}
		catch (IOException ie)
		{
			AlertBox.display("오류", ip + ": IOException");
		}
		catch (IllegalArgumentException iae)
		{
			AlertBox.display("오류", ip + ": IllegalArgumentException");
		}
		
		return;
	}
	
	private void ipIncrease(int[] ip)
	{
		if (++ip[3] > 255)
		{
			ip[3] = 0;
			if (++ip[2] > 255)
			{
				ip[2] = 0;
				if (++ip[1] > 255)
				{
					ip[1] = 0;
					++ip[0];
				}
			}
		}
		
		return;
	}

	public PingTestResult start(PcRoom p)
	{
		ArrayList<String[]> ipList = p.getIpList();
		long totalPcCount = UtilForIpList.countPcCount(ipList);
		
		threadsCounter = new AtomicInteger(0);
		atomicTurnOnPcCount = new AtomicLong(0);
		remain = new AtomicLong(totalPcCount);
		
		LocalDateTime startTime = LocalDateTime.now();
		
		for (String[] ipArray : ipList)
		{
			int[][] splitIp = new int[2][4];
			
			for (int a = 0; a < splitIp.length; a++)
			{
				String[] splitIpString = ipArray[a].split("\\.");
				for (int b = 0; b < splitIp[a].length; b++)
				{
					splitIp[a][b] = Integer.parseInt(splitIpString[b]);
				}
			}
			
			ipIncrease(splitIp[1]);
			
			while (!Arrays.equals(splitIp[0], splitIp[1]))
			{
				String ipString = "";
				
				for (int ipDigit : splitIp[0])
				{
					ipString += ipDigit + ".";
				}
				
				PingTestThread mt = new PingTestThread(ipString.substring(0, ipString.length() - 1));
				Thread t = new Thread(mt);
				
				while (threadsCounter.get() >= MAX_THREADS)
				{
					try
					{
						Thread.sleep(DELAY);
					}
					catch (InterruptedException e)
					{
						// AlertBox.display("오류", "쓰레드 인터럽트가 발생했습니다. [1]");
					}
				}
				
				t.start();
				
				ipIncrease(splitIp[0]);
			}
		}
		
		while (threadsCounter.get() > 0 || remain.get() > 0)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// AlertBox.display("오류", "쓰레드 인터럽트가 발생했습니다. [2]");
			}
		}
		
		return new PingTestResult(p.getName(), totalPcCount, atomicTurnOnPcCount.get(), startTime, LocalDateTime.now());
	}
}
