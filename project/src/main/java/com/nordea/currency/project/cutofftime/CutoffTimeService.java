package com.nordea.currency.project.cutofftime;

import java.time.LocalDate;

public interface CutoffTimeService {
	public String getCutoffTime(String ISO1, String ISO2, LocalDate date) throws Exception;
}
