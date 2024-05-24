package com.os.contractexport.tasks;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RecordReader {

	public LocalDate nextWorkingDay(LocalDate startDate) {
		
		LocalDate nextDate = startDate.plusDays(1);
		
		if (nextDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
			nextDate = nextDate.plusDays(2);
		} else if (nextDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			nextDate = nextDate.plusDays(1);
		}

		return nextDate;
	}

}
