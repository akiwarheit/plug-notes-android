package com.plug.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateFormat;
import android.util.Log;

public class DateUtils {


	private static Long date;
	private static Date dateObj;
	private static SimpleDateFormat formater;
	private static android.text.format.DateFormat df;

	private static String stringDate;

	public DateUtils(long l) {
		DateUtils.date = l;
		Log.e("dateOrig", "" + l);

		df = new android.text.format.DateFormat();
		stringDate = (String) DateFormat.format("yyyy-MM-dd k:mm:ss", l);
		setStringDate(stringDate);
		Log.e("dateString", "" + stringDate);

		formater = new SimpleDateFormat("yyyy-MM-dd k:mm:ss");
		try {
			dateObj = formater.parse(stringDate);
			setDateObj(dateObj);
			Log.e("dateObj", "" + dateObj);

		} catch (Exception e) {
			Log.e("Exception", "" + e.getMessage());
		}
	}

	public void setDate(Long date) {
		DateUtils.date = date;
	}

	public Long getDate() {
		return date;
	}

	public void setStringDate(String stringDate) {
		DateUtils.stringDate = stringDate;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setDateObj(Date dateObj) {
		DateUtils.dateObj = dateObj;
	}

	public Date getDateObj() {
		return dateObj;
	}
}
