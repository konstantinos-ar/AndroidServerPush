package com.metrictrade.pushserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class NotificationSend implements iRun
{

	protected int _loadtype;
	protected boolean _running;
	int logFilter;
	int secInterval = 30;
	public iConfig _config;
	private SimpleDateFormat _fileDateFormat = new SimpleDateFormat("yyyy_MM_dd");
	String  _logpath;
	public static final int LOG_INFO	=0x1;
	public static final int LOG_SELECT	=0x40;
	public static final int LOG_DEBUG	=0x80;

	static cLog	_logger;
	protected   boolean loggercreated = false;


	public NotificationSend()
	{
		_logger = new cLog();
		_logpath = "../"+File.separator+"logs"+File.separator+"ztrade"+File.separator;
	}

	public void start()
			throws Exception
			{
		if (isRunning())
			throw new IllegalStateException("Started");
		_running = true;

		sendnotification();
			}

	public void stop()
			throws InterruptedException
			{
		if (isRunning())
		{
			_running = false;
			_logger.log("Stoping push notifications.");
		}
			}

	public void setLogDebug(boolean set)
	{
		updateLog(set, LOG_DEBUG);
	}

	public boolean getLogDebug()
	{
		return getLog(LOG_DEBUG);
	}

	public void setPrintWriter(PrintWriter  out)
	{
		_logger.setExtWriter(out);
	}

	public void setSecInterval(int s)
	{
		if (s > 0)
			secInterval = s;
	}

	public int getSecInterval()
	{
		return secInterval;
	}

	public void setLogInfo(boolean set)
	{
		updateLog(set, LOG_INFO);
	}

	public boolean getLogInfo()
	{
		return getLog(LOG_INFO);
	}

	public void setLogSelect(boolean set)
	{
		updateLog(set, LOG_SELECT);
	}

	public boolean getLogSelect()
	{
		return getLog(LOG_SELECT);
	}

	private void updateLog(boolean set, int tg)
	{
		if (set)
			logFilter |= tg;
		else
			logFilter &= ~tg;
	}

	private boolean getLog(int tg)
	{
		return (logFilter & tg) != 0;
	}


	public void destroy()
	{	
		_running = false;
	}

	public boolean isRunning()
	{
		return _running;
	}

	public void init(iConfig config)
	throws IOException
	{
		_config = config;

		if (!loggercreated)
			_logger.createLogger(_logpath, "push", 90);
		loggercreated = true;

	}
	
	public String getParameter(String key)
	{
		return _config.getParameter(key);
	}
	
	private void sendnotification()
	{
			int state = 0;
			MonitorI mo = new MonitorI();
			mo.setGroup(',');
			mo.setDecimal('.');
			
			boolean inited = false;
			IntHash trnumbers = new IntHash();
			HashMap smsnumbers = new HashMap();
			HashMap personnumbers = new HashMap();
			int lastO = 0, lastT = 0;
			StringBuilder sb = new StringBuilder(160);
			String bo = getParameter("connectPUSH");
			DBPool pool = null;

			_logger.log("Starting push notifications...");

			if (bo != null && StringIO.getColString(bo,0,',') != null)
			{
				try {
					pool = new DBPool(StringIO.getColString(bo,0,','), StringIO.getColString(bo,1,','), StringIO.getColString(bo,2,','));
				} catch (SQLException e) {

				}
			}
			
			for (int k = 0;  _running; ++k)
			{
				try {
					for (int i = 0; _running &&  i < 10; ++i )
						Thread.sleep(secInterval*100);
				} catch (InterruptedException e) {
					break;
				}
				int ost = mo.getOrderState();
				if (state != ost)
				{
					state = ost;
					inited = false; 
					k = 0;
				}


				if (!inited)
				{
					inited = true;
					
					try {
						java.util.Date now = new java.util.Date();
						BufferedReader in = new BufferedReader(new FileReader(_logpath+"push"+_fileDateFormat.format(now)+".log"));
						String s = in.readLine();
						while (s != null)
						{
							if (s.indexOf(" TR: ") >= 0)
							{
								trnumbers.put(Integer.parseInt(s.substring(s.indexOf(" TR: ")+5)), "push");
							}
							s = in.readLine();
						}
					}
					catch (IOException e)
					{
						_logger.log("Error1: " + e);
					}
				}
				Connection conn = null;
	            Statement st = null;

	            if ((k % 3) == 0)
				{	
					try {
						//Connection to database and fetch of records (Cannot post code..)
					}catch (Exception e)
					{
						_logger.log("Error2: " + e);
					}
					DBPool.close(conn, st, null);
					conn = null;
					st = null;
				}
				//Load user orders and trades and create the message (Cannot post code..)
				
					}
				}
				DBPool.close(conn, st, null);
				conn = null;
				st = null;
			}
			_logger.log("exit");
			_running = false;
	}

}