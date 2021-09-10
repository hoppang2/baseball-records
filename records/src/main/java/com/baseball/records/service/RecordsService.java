package com.baseball.records.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class RecordsService extends _DefaultService {

	public void yearRecordsSetting() {
		Calendar c = Calendar.getInstance();

		c.set(c.get(Calendar.YEAR), 2, 1);
		Date date = c.getTime();
	}
}
