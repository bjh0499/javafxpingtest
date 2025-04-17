package pcRoomList.obj;

import java.io.*;
import java.util.*;

import javafx.beans.property.*;
import pcRoomList.modal.*;
import pcRoomList.util.*;

public class PcRoomForTable implements Serializable
{
	private static final long serialVersionUID = -8209986297400296813L;
	private final StringProperty nameProperty;
	private final ArrayList<StringProperty[]> ipList;
	private final LongProperty pcCountProperty;
	
	public PcRoomForTable(PcRoom pcRoom)
	{
		this.nameProperty = new SimpleStringProperty(pcRoom.getName());
		this.ipList = UtilForPcRoomList.convertIpListProperty(pcRoom.getIpList());
		this.pcCountProperty = new SimpleLongProperty(refreshPcCount());
	}
	
	public PcRoomForTable(String name, ArrayList<String[]> originalIpList)
	{
		this.nameProperty = new SimpleStringProperty(name);
		this.ipList = UtilForPcRoomList.convertIpListProperty(originalIpList);
		this.pcCountProperty = new SimpleLongProperty(refreshPcCount());
	}
	
	
	public StringProperty getNameProperty()
	{
		return nameProperty;
	}
	
	public String getName()
	{
		return nameProperty.get();
	}
	
	public void setName(String name)
	{
		nameProperty.set(name);
	}
	
	public ArrayList<StringProperty[]> getIpList()
	{
		return ipList;
	}
	
	public void refreshIpList(ArrayList<StringProperty[]> ipList)
	{
		this.ipList.clear();
		
		for (StringProperty[] ip : ipList)
		{
			this.ipList.add(ip);
		}
		
		pcCountProperty.set(refreshPcCount());
	}

	public LongProperty getPcCountProperty()
	{
		return pcCountProperty;
	}
	
	public long getPcCount()
	{
		return pcCountProperty.get();
	}
	
	private long refreshPcCount()
	{
		long result = 0;
		ArrayList<StringProperty[]> ipList = this.getIpList();
		
		if (ipList != null)
		{
			for (StringProperty[] ip : ipList)
			{
				String startIp = ip[0].get();
				String endIp = ip[1].get();
				
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pcCountProperty == null) ? 0 : pcCountProperty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PcRoomForTable other = (PcRoomForTable) obj;
		if (pcCountProperty == null)
		{
			if (other.pcCountProperty != null)
				return false;
		}
		else if (!pcCountProperty.equals(other.pcCountProperty))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "PcRoomForTable [pcCount=" + pcCountProperty + ", name=" + nameProperty + ", ipList=" + ipList + "]";
	}
}
