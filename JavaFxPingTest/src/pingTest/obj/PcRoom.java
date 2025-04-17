package pingTest.obj;

import java.io.*;
import java.util.*;

public class PcRoom implements Serializable
{
	private static final long serialVersionUID = 2451178950018692407L;
	private String name;
	private ArrayList<String[]> ipList;
	
	protected PcRoom()
	{
		
	}
	
	public PcRoom(String name, ArrayList<String[]> ipList)
	{
		this.name = name;
		this.ipList = ipList;
	}
	
	public PcRoom(PcRoom pcRoom)
	{
		this.name = pcRoom.name;
		this.ipList = pcRoom.ipList;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ArrayList<String[]> getIpList()
	{
		return ipList;
	}

	public void setIpList(ArrayList<String[]> ipList)
	{
		this.ipList = ipList;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipList == null) ? 0 : ipList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PcRoom other = (PcRoom) obj;
		if (ipList == null)
		{
			if (other.ipList != null)
				return false;
		}
		else if (!ipList.equals(other.ipList))
			return false;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "PcRoom [name=" + name + ", ipList=" + ipList + "]";
	}
}