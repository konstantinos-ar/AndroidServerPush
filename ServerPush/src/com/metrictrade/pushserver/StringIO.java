package com.metrictrade.pushserver;

import java.util.StringTokenizer;

public class StringIO
{
	public StringIO()
	{

	}
		public static int getColIndex(String s, int colindex, int off, int colsep)
	{
		if (null != s)
		{
			int slen = s.length();
	
			for (; colindex > 0 && off < slen; ++off)
				if (s.charAt(off) == colsep)
					colindex--;
		}
		return off;
	}

	public static String getColString(String s, int colindex, int colsep)
	{
		return getColString(s, colindex, 0, colsep);
	}

	public static String getColString(String s, int colindex, int off, int colsep)
	{
		if (null == s)
			return null;

		int end, slen = s.length();

		off = getColIndex(s, colindex, off, colsep);
		
		for (end=off; end < slen; ++end)
			if (s.charAt(end) == colsep)
				break;

		if (off >= end)
			return null;

		return s.substring(off, end);
	}

	public static double getColDouble(String s, int colindex, int colsep)
	{
		return getColDouble(s, colindex, 0, colsep);
	}
	
	public static double getColDouble(String s, int colindex, int off, int colsep)
	{
		return parseMidDouble(s, getColIndex(s, colindex, off, colsep));
	}

	public static double parseMidDouble(String s, int offset)
	{
		
		int  neg = 1, digit = 0, slen = s.length();
		double v = 0, dec = 0;
		char c;

		if (s != null)
		for(int i = offset; i < slen; i++)
		{	
			c = s.charAt(i);
			if (c == '.' || c == ',')
				dec = 1;
			else if (Character.isDigit(c))
			{	v = v*10 + (c - '0');
				dec *= 10;
				digit++;
			}
			else if (digit > 0)
				break;
			else if (c == '-')
				neg = -1;
			else if (c != '+' && !Character.isWhitespace(c))
				break;
		}
		return neg*(dec > 0? v/dec: v);
	}
	
	public static int parseAnyInt(String s, int offset)
	{
		int  v = 0, digit = 0, slen = s.length();
		char c;

		if (s != null)
		for(;offset < slen; offset++)
		{	
			c = s.charAt(offset);
			if (Character.isDigit(c))
			{	v = v*10 + (c - '0');
				digit++;
			}
			else if (digit > 0)
				break;
		}
		return v;
	}

	public static String[] arrayFromToken(String s, String delim)
	{
		if (s == null)
			return null;

		StringTokenizer tokens = new StringTokenizer(s,delim);
		int count = tokens.countTokens();

		if (count == 0)
			return null;
		String array[]	= new String[count];

		for (int i = 0; i < count; ++i)
			array[i] = tokens.nextToken();

		return array;
	}

	public static double getDouble(String s, int length)
	{

		if (null == s || length <= 0)
			return 0;

		char b;
		double v = 0, dec = 0;
		int  minus = 1;

		for (int i = 0; i < length; ++i)
		{
			b = s.charAt(i);

			if (b == '+' || b == '-' || b == '.')
			{	if (b == '-')
					minus = -1;
				if (b == '.')
					dec = 1;
				continue;
			}
			if (b < '0' || b > '9')
				break;
			dec *= 10;
			v *= 10;
			v += (b-'0');
		}
		return (dec == 0)? minus*v: minus*v/dec;
	}

	public static long getLongId(String s)
	{

		if (null == s)
			return 0;
		int length = s.length();

		char b;
		long v = 0;

		for (int i = 0; i < length; ++i)
		{
			b = s.charAt(i);

			if (b < '0' || b > '9')
				continue;
			v = v*10 + (b-'0');
		}
		return v;
	}
}	
