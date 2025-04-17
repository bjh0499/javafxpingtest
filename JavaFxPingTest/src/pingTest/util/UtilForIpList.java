package pingTest.util;

import java.util.*;

import pingTest.modal.*;

public class UtilForIpList
{
	public static boolean isLegalIp(String ip)
	{
		return ip.matches("(?:(?:2(?:[0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9])\\.){3}(?:(?:2([0-4][0-9]|5[0-5])|[0-1]?[0-9]?[0-9]))");
	}
	
	public static long ipToLong(String ip) throws Exception
	{
		if (isLegalIp(ip))
		{
			long result = 0;
			String[] splitIpString = ip.split("\\.");
			
			for (int b = 0; b < splitIpString.length; b++)
			{
				result += Long.parseLong(splitIpString[b]) << (8 * (3 - b));
			}
			
			return result;
		}
		else
		{
			throw new Exception("Illegal IP");
		}
	}
	
	public static boolean isLegalIpRange(String startIp, String endIp) throws Exception
	{
		return ipToLong(endIp) - ipToLong(startIp) >= 0;
	}
	
	public static boolean isDuplicatedIp(String baseStartIp, String baseEndIp, String newStartIp, String newEndIp) throws Exception
	{
		long newStartIpLong = ipToLong(newStartIp);
		long newEndIpLong = ipToLong(newEndIp);
		long baseStartIpLong = ipToLong(baseStartIp);
		long baseEndIpLong = ipToLong(baseEndIp);
		
		return (newStartIpLong >= baseStartIpLong && newStartIpLong <= baseEndIpLong) || (newEndIpLong >= baseStartIpLong && newEndIpLong <= baseEndIpLong);
	}
	
	public static void ipListSort(ArrayList<String[]> ipList)
	{
		ipList.sort(new Comparator<String[]>()
		{
			@Override
			public int compare(String[] o1, String[] o2)
			{
				long ip1Long = 0;
				long ip2Long = 0;
				try
				{
					ip1Long = ipToLong(o1[0]);
					ip2Long = ipToLong(o2[0]);
				}
				catch (Exception e)
				{
					AlertBox.display("오류", "IP 정렬 중 오류 발생.");
				}
				
				long result = ip1Long - ip2Long;
				if (result > 0)
				{
					return 1;
				}
				else if (result < 0)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}
		});
		
		return;
	}
	
	public static long countPcCount(ArrayList<String[]> ipList)
	{
		long result = 0;
		
		if (ipList != null)
		{
			for (String[] ip : ipList)
			{
				String startIp = ip[0];
				String endIp = ip[1];
				
				try
				{
					long startIpToLong = UtilForIpList.ipToLong(startIp);
					long endIpToLong = UtilForIpList.ipToLong(endIp);
					
					if (endIpToLong < 0 || startIpToLong < 0)
					{
						result = -1;
						break;
					}
					
					result += (endIpToLong - startIpToLong + 1);
				}
				catch (Exception e)
				{
					AlertBox.display("오류", "PC 수 갱신 중 오류가 발생했습니다.");
					break;
				}
			}
		}
		
		return result;
	}
}
