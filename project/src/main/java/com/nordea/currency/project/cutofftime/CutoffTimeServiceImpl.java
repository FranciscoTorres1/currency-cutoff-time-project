package com.nordea.currency.project.cutofftime;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nordea.currency.project.currency.Currency;
import com.nordea.currency.project.currency.CurrencyRepository;

@Service
public class CutoffTimeServiceImpl implements CutoffTimeService {
	@Autowired
	private CurrencyRepository currencyRepo;

	@Override
	public String getCutoffTime(String ISO1, String ISO2, LocalDate dateOfTrade) {
		String deadlineCutoffTime;

		Currency currency1 = currencyRepo.findFirstByISO(ISO1);
		checkForValidCurrency(ISO1, currency1);

		Currency currency2 = currencyRepo.findFirstByISO(ISO2);
		checkForValidCurrency(ISO2, currency2);

		String currency1Date = getCutoffDayByDateOfTrade(currency1.getCutoffTime(), dateOfTrade);
		String currency2Date = getCutoffDayByDateOfTrade(currency2.getCutoffTime(), dateOfTrade);

		deadlineCutoffTime = getDeadlineCutoffTimeFromCurrencyPair(currency1Date, currency2Date);

		return deadlineCutoffTime;
	}

	private void checkForValidCurrency(String ISO, Currency currency) {
		if (currency == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ISO + " is a invalid currency");
	}

	private String getCutoffDayByDateOfTrade(CutoffTime cutoffTime, LocalDate dateOfTrade) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);

		if (LocalDate.now().isEqual(dateOfTrade)) {
			return cutoffTime.getToday();
		} else if (LocalDate.now().plusDays(1).isEqual(dateOfTrade)) {
			return cutoffTime.getTomorrow();
		} else if (dateOfTrade.isAfter(tomorrow)) {
			return cutoffTime.getAfterTomorrow();
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date is before today");
		}
	}

	private String getDeadlineCutoffTimeFromCurrencyPair(String currency1Date, String currency2Date) {
		String deadlineCutoffTime;

		if (currency1Date.equals("Never possible") || currency2Date.equals("Never possible")) {
			deadlineCutoffTime = "Never possible";
		} else if (currency1Date.equals("Always possible")) {
			deadlineCutoffTime = currency2Date;
		} else if (currency2Date.equals("Always possible")) {
			deadlineCutoffTime = currency1Date;
		} else {
			deadlineCutoffTime = LocalTime.parse(currency1Date).isBefore(LocalTime.parse(currency2Date)) ? currency1Date
					: currency2Date;
		}
		return deadlineCutoffTime;
	}

}
