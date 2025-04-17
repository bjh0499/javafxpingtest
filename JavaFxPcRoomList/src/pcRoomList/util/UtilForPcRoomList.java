package pcRoomList.util;

import java.util.*;

import javafx.beans.property.*;

public class UtilForPcRoomList
{
	public static ArrayList<StringProperty[]> convertIpListProperty (ArrayList<String[]> originalIpList)
	{
		ArrayList<StringProperty[]> ipList = new ArrayList<StringProperty[]>();
		
		for (String[] originalIp : originalIpList)
		{
			StringProperty[] convertedIp = new StringProperty[2];
			convertedIp[0] = new SimpleStringProperty(originalIp[0]);
			convertedIp[1] = new SimpleStringProperty(originalIp[1]);
			ipList.add(convertedIp);
		}
		
		return ipList;
	}
	
	public static ArrayList<String[]> convertIpList (ArrayList<StringProperty[]> originalIpListProperty)
	{
		ArrayList<String[]> ipList = new ArrayList<String[]>();
		
		for (StringProperty[] originalIp : originalIpListProperty)
		{
			String[] convertedIp = new String[2];
			convertedIp[0] = originalIp[0].get();
			convertedIp[1] = originalIp[1].get();
			ipList.add(convertedIp);
		}
		
		return ipList;
	}
}
