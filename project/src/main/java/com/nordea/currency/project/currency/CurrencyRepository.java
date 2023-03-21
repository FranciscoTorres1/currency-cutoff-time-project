package com.nordea.currency.project.currency;

import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {
	public Currency findFirstByISO(String ISO);
}
