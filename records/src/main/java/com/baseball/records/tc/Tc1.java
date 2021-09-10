package com.baseball.records.tc;

import java.util.Calendar;
import java.util.Date;

import com.baseball.records.utils.WebPackage;

public class Tc1 {

	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();

		System.out.println(WebPackage.DateToString(now, "yyyy-MM-dd"));
		System.out.println(c.getTime());
		System.out.println(c.get(Calendar.YEAR));
		System.out.println(c.get(c.MONTH));
		System.out.println(c.get(c.DAY_OF_MONTH));

		int year = c.get(Calendar.YEAR) + 1;
		int month = c.get(Calendar.MONTH);
		int day = 1;

		c.set(year, 2, day);
		System.out.println(WebPackage.DateToString(c.getTime(), "yyyy-MM-dd"));

	}

}
