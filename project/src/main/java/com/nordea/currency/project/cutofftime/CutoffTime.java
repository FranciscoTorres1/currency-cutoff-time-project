package com.nordea.currency.project.cutofftime;

import com.nordea.currency.project.currency.Currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class CutoffTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "today")
	private String today;

	@Column(name = "tomorrow")
	private String tomorrow;

	@Column(name = "after_tomorrow")
	private String afterTomorrow;

	@OneToOne(mappedBy = "cutoffTime")
	private Currency currency;

	protected CutoffTime() {
	}

	public CutoffTime(String today, String tomorrow, String afterTomorrow) {
		this.today = today;
		this.tomorrow = tomorrow;
		this.afterTomorrow = afterTomorrow;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getToday() {
		return today;
	}

	public void setToday(String today) {
		this.today = today;
	}

	public String getTomorrow() {
		return tomorrow;
	}

	public void setTomorrow(String tomorrow) {
		this.tomorrow = tomorrow;
	}

	public String getAfterTomorrow() {
		return afterTomorrow;
	}

	public void setAfterTomorrow(String afterTomorrow) {
		this.afterTomorrow = afterTomorrow;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
