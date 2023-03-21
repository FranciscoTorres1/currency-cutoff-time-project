package com.nordea.currency.project.cutofftime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import com.nordea.currency.project.currency.Currency;
import com.nordea.currency.project.currency.CurrencyRepository;

@ExtendWith(SpringExtension.class)
public class CutoffTimeServiceImplIntegrationTest {
	@TestConfiguration
	static class CutoffTimeServiceImplTestContextConfiguration {
		@Bean
		public CutoffTimeService cutoffTimeService() {
			return new CutoffTimeServiceImpl();
		}
	}

	@Autowired
	private CutoffTimeService cutoffTimeService;

	@MockBean
	private CurrencyRepository currencyRepository;

	@BeforeEach
	public void initEach() {
		String AED = "AED";
		String PLN = "PLN";
		String RUB = "RUB";
		String CHF = "CHF";

		CutoffTime cutoffTimeAED = new CutoffTime("Never possible", "14:00", "Always possible");
		CutoffTime cutoffTimePLN = new CutoffTime("10:00", "Always possible", "Always possible");
		CutoffTime cutoffTimeRUB = new CutoffTime("Never possible", "13:00", "Always possible");
		CutoffTime cutoffTimeCHF = new CutoffTime("10:00", "Always possible", "Always possible");

		Currency currencyAED = new Currency(AED, "United Arab Emirates", cutoffTimeAED);
		Currency currencyPLN = new Currency(PLN, "Poland", cutoffTimePLN);
		Currency currencyRUB = new Currency(RUB, "RUSSIA", cutoffTimeRUB);
		Currency currencyCHF = new Currency(CHF, "Switzerland", cutoffTimeCHF);

		when(currencyRepository.findFirstByISO(currencyAED.getISO())).thenReturn(currencyAED);
		when(currencyRepository.findFirstByISO(currencyPLN.getISO())).thenReturn(currencyPLN);
		when(currencyRepository.findFirstByISO(currencyRUB.getISO())).thenReturn(currencyRUB);
		when(currencyRepository.findFirstByISO(currencyCHF.getISO())).thenReturn(currencyCHF);
		when(currencyRepository.findFirstByISO("Not_a_valid_ISO_name")).thenReturn(null);
	}

	@ParameterizedTest
	@CsvSource({ "0,Never possible", "1,13:00", "2,Always possible" })
	public void givenValidCurrenciesAndDateOfTrade_WhenGetCutoffTime_thenReturnCutoffTime(String dayIncrement,
			String expectedCutoffTime) {
		String PLN = "PLN";
		String RUB = "RUB";
		int dayIncrementInt = Integer.parseInt(dayIncrement);
		LocalDate day = LocalDate.now().plusDays(dayIncrementInt);

		try {
			String cutoffTimeFound = cutoffTimeService.getCutoffTime(PLN, RUB, day);

			assertThat(cutoffTimeFound).isEqualTo(expectedCutoffTime);
		} catch (Exception e) {
			fail("Should not throw exception");
		}
	}

	@Test
	public void givenOtherValidCurrenciesAndDateOfTrade_WhenGetCutoffTime_thenReturnCutoffTime() {
		String expectedCutoffTime = "14:00";
		String AED = "AED";
		String CHF = "CHF";
		LocalDate tomorrow = LocalDate.now().plusDays(1);

		try {
			String cutoffTimeFound = cutoffTimeService.getCutoffTime(AED, CHF, tomorrow);

			assertThat(cutoffTimeFound).isEqualTo(expectedCutoffTime);
		} catch (Exception e) {
			fail("Should not throw exception");
		}
	}

	@Test
	public void givenValidCurrenciesWhichBothHaveTimesAndDateOfTrade_WhenGetCutoffTime_thenReturnCutoffTime() {
		String expectedCutoffTime = "13:00";
		String AED = "AED";
		String RUB = "RUB";
		LocalDate tomorrow = LocalDate.now().plusDays(1);

		try {
			String cutoffTimeFound = cutoffTimeService.getCutoffTime(AED, RUB, tomorrow);

			assertThat(cutoffTimeFound).isEqualTo(expectedCutoffTime);
		} catch (Exception e) {
			fail("Should not throw exception");
		}
	}

	@Test
	public void givenInvalidCurrency_WhenGetCutoffTime_thenThrowBadRequest() {
		String notValidISO = "Not_a_valid_ISO_name";
		String RUB = "RUB";
		String expectedExceptionMsg = "400 BAD_REQUEST \"Not_a_valid_ISO_name is a invalid currency\"";
		LocalDate today = LocalDate.now();

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			cutoffTimeService.getCutoffTime(notValidISO, RUB, today);
		});

		assertThat(exception.getMessage()).isEqualTo(expectedExceptionMsg);
	}

	@Test
	public void givenInvalidDate_WhenGetCutoffTime_thenThrowBadRequest() {
		String PLN = "PLN";
		String RUB = "RUB";
		String expectedExceptionMsg = "400 BAD_REQUEST \"The date is before today\"";
		LocalDate yesterday = LocalDate.now().minusDays(1);

		Exception exception = assertThrows(ResponseStatusException.class, () -> {
			cutoffTimeService.getCutoffTime(PLN, RUB, yesterday);
		});

		assertThat(exception.getMessage()).isEqualTo(expectedExceptionMsg);
	}
}
