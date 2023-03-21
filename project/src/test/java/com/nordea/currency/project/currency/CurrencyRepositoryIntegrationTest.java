package com.nordea.currency.project.currency;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.nordea.currency.project.cutofftime.CutoffTime;

//data.sql in src/main/resource already inserts data
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CurrencyRepositoryIntegrationTest {
	@Autowired
	CurrencyRepository currencyRepository;

	@Test
	public void whenFindByISO_thenReturnCurrency() {
		String AED = "AED";
		CutoffTime cutoffTimeAED = new CutoffTime("Never possible", "14:00", "Always possible");
		Currency currencyAED = new Currency(AED, "United Arab Emirates", cutoffTimeAED);

		Currency currencyFound = currencyRepository.findFirstByISO(AED);

		assertThat(currencyFound.getISO()).isEqualTo(currencyAED.getISO());
		assertThat(currencyFound.getCountry()).isEqualTo(currencyAED.getCountry());
	}

	@Test
	public void whenInvalidISO_thenReturnNull() {
		String invalidISO = "AED2";

		Currency currencyFound = currencyRepository.findFirstByISO(invalidISO);

		assertThat(currencyFound).isNull();
	}
}
