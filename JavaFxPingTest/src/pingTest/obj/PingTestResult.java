package pingTest.obj;

import java.io.*;
import java.time.*;

public class PingTestResult implements Serializable
{
	private static final long serialVersionUID = 8560926964091147900L;
	private String PcRoomName;
	private long totalPcCount;
	private long turnOnPcCount;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public PingTestResult()
	{
		
	}
	
	public PingTestResult(String pcRoomName, long totalPcCount, long turnOnPcCount, LocalDateTime startTime,
			LocalDateTime endTime)
	{
		super();
		PcRoomName = pcRoomName;
		this.totalPcCount = totalPcCount;
		this.turnOnPcCount = turnOnPcCount;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getPcRoomName()
	{
		return PcRoomName;
	}

	public void setPcRoomName(String pcRoomName)
	{
		PcRoomName = pcRoomName;
	}

	public long getTotalPcCount()
	{
		return totalPcCount;
	}

	public void setTotalPcCount(long totalPcCount)
	{
		this.totalPcCount = totalPcCount;
	}

	public long getTurnOnPcCount()
	{
		return turnOnPcCount;
	}

	public void setTurnOnPcCount(long turnOnPcCount)
	{
		this.turnOnPcCount = turnOnPcCount;
	}

	public LocalDateTime getStartTime()
	{
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime)
	{
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime()
	{
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime)
	{
		this.endTime = endTime;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PcRoomName == null) ? 0 : PcRoomName.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + (int) (totalPcCount ^ (totalPcCount >>> 32));
		result = prime * result + (int) (turnOnPcCount ^ (turnOnPcCount >>> 32));
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
		PingTestResult other = (PingTestResult) obj;
		if (PcRoomName == null)
		{
			if (other.PcRoomName != null)
				return false;
		}
		else if (!PcRoomName.equals(other.PcRoomName))
			return false;
		if (endTime == null)
		{
			if (other.endTime != null)
				return false;
		}
		else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null)
		{
			if (other.startTime != null)
				return false;
		}
		else if (!startTime.equals(other.startTime))
			return false;
		if (totalPcCount != other.totalPcCount)
			return false;
		if (turnOnPcCount != other.turnOnPcCount)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "PingTestResult [PcRoomName=" + PcRoomName + ", totalPcCount=" + totalPcCount + ", turnOnPcCount="
				+ turnOnPcCount + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
