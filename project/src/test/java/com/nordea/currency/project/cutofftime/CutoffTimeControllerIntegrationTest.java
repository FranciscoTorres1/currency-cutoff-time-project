package com.nordea.currency.project.cutofftime;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CutoffTimeController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CutoffTimeControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CutoffTimeService service;

	@Test
	public void givenTwoCurrenciesAndADateofTrade_whenGetCutoffTime_thenReturnDate() throws Exception {
		String AED = "AED";
		String PLN = "PLN";
		LocalDate today = LocalDate.now();
		String deadlineCutoffTime = "Never Possible";

		given(service.getCutoffTime(AED, PLN, today)).willReturn(deadlineCutoffTime);

		mvc.perform(get("/cutoffTime/{ISO1}/{ISO2}", AED, PLN).param("dateOfTrade", today.toString()))
				.andExpect(status().isOk()).andExpect(
						result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(deadlineCutoffTime));
		verify(service, VerificationModeFactory.times(1)).getCutoffTime(anyString(), anyString(), any());
	}

	@Test
	public void givenTwoCurrenciesAndADateIsYesterday_whenGetCutoffTime_thenReturnBadRequest() throws Exception {
		String AED = "AED";
		String PLN = "PLN";
		LocalDate yesterday = LocalDate.now().minusDays(1);
		String expectedErrorMsg = "400 BAD_REQUEST \"The date is before today\"";

		doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The date is before today")).when(service)
				.getCutoffTime(anyString(), anyString(), any());
		
		mvc.perform(get("/cutoffTime/{ISO1}/{ISO2}", AED, PLN).param("dateOfTrade", yesterday.toString()))
				.andExpect(status().isBadRequest())
				.andExpect(result -> assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedErrorMsg));
		verify(service, VerificationModeFactory.times(1)).getCutoffTime(anyString(), anyString(), any());
	}
}
