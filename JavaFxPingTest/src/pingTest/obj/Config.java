package pingTest.obj;

import java.io.*;

public class Config implements Serializable
{
	private static final long serialVersionUID = -8747602085913121944L;
	private int maxThreads;
	private int limitTime;
	private int delay;
	private int defaultHour;
	private int defaultMinute;
	private int defaultSecond;
	
	public Config()
	{
		
	}
	
	public Config(int maxThreads, int limitTime, int delay, int defaultHour, int defaultMinute, int defaultSecond)
	{
		this.maxThreads = maxThreads;
		this.limitTime = limitTime;
		this.delay = delay;
		this.defaultHour = defaultHour;
		this.defaultMinute = defaultMinute;
		this.defaultSecond = defaultSecond;
	}

	public int getMaxThreads()
	{
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads)
	{
		this.maxThreads = maxThreads;
	}

	public int getLimitTime()
	{
		return limitTime;
	}

	public void setLimitTime(int limitTime)
	{
		this.limitTime = limitTime;
	}

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	public int getDefaultHour()
	{
		return defaultHour;
	}

	public void setDefaultHour(int defaultHour)
	{
		this.defaultHour = defaultHour;
	}

	public int getDefaultMinute()
	{
		return defaultMinute;
	}

	public void setDefaultMinute(int defaultMinute)
	{
		this.defaultMinute = defaultMinute;
	}

	public int getDefaultSecond()
	{
		return defaultSecond;
	}

	public void setDefaultSecond(int defaultSecond)
	{
		this.defaultSecond = defaultSecond;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + defaultHour;
		result = prime * result + defaultMinute;
		result = prime * result + defaultSecond;
		result = prime * result + delay;
		result = prime * result + limitTime;
		result = prime * result + maxThreads;
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
		Config other = (Config) obj;
		if (defaultHour != other.defaultHour)
			return false;
		if (defaultMinute != other.defaultMinute)
			return false;
		if (defaultSecond != other.defaultSecond)
			return false;
		if (delay != other.delay)
			return false;
		if (limitTime != other.limitTime)
			return false;
		if (maxThreads != other.maxThreads)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Config [maxThreads=" + maxThreads + ", limitTime=" + limitTime + ", delay=" + delay + ", defaultHour="
				+ defaultHour + ", defaultMinute=" + defaultMinute + ", defaultSecond=" + defaultSecond + "]";
	}
}
