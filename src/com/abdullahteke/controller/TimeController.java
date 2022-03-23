package com.abdullahteke.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;


public class TimeController {
	

	private static TimeController managerInstance;

	
	public static TimeController getManagerInstance() {
		
		if (managerInstance==null){
			managerInstance=new TimeController();
		}
		return managerInstance;
	}


	public long get5MinutesBefore(){
		
		Calendar cal= Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -5);
		return cal.getTimeInMillis();
	}
	
	
	public long getCurrentDateTime() {
		Calendar cal= Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTimeInMillis();

	}
	
	public int abc(String Value) {
		int retVal=0;
		
		DecimalFormat df = new DecimalFormat();
		Number num = df.parse(Value, new ParsePosition(0));
		retVal = (int) num.doubleValue();
		System.out.println(retVal); // This prints 123
		return Math.round(retVal);
	}

} // end of Manager Class
