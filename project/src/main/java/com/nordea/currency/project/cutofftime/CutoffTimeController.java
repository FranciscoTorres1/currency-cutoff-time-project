package com.nordea.currency.project.cutofftime;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CutoffTimeController {
	@Autowired
	CutoffTimeService cutoffTimeService;

	/**
	 * 
	 * Returns the earliest cut-off time for a currency pair on a given date.
	 * 
	 * @param dateOfTrade The given date of the trade between the currencies
	 * @param currency1   The first currency as ISO
	 * @param currency2   The second currency as ISO
	 * @return
	 */
	@GetMapping("/cutoffTime/{currency1}/{currency2}")
	ResponseEntity<String> getCutoffTime(
			@RequestParam("dateOfTrade") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfTrade,
			@PathVariable String currency1, @PathVariable String currency2) {
		try {
			return new ResponseEntity<String>(cutoffTimeService.getCutoffTime(currency1, currency2, dateOfTrade),
					HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
