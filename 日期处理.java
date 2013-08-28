//根据当前日期获取其long型时间
public static long getLongtimeForDate(int year, int month, int day){
		String temp = String.format("%d-%02d-%02d", year,
				month, day);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try
		{
			date = formatter.parse(temp);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}
	
	//根据long型时间获取日期
	public static String getDateByLongTime(long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(new java.sql.Timestamp(time));
	}
	
	//根据当前系统时间获取日期
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String format = formatter.format(new java.sql.Timestamp(System.currentTimeMillis()));
		System.out.println(format);
