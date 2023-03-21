package com.nordea.currency.project.currency;

import com.nordea.currency.project.cutofftime.CutoffTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Currency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "iso")
	private String ISO;

	@Column(name = "country")
	private String country;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cutofftime_id", referencedColumnName = "id")
	private CutoffTime cutoffTime;

	public Currency() {
	}

	public Currency(String ISO, String country, CutoffTime cutoffTime) {
		this.ISO = ISO;
		this.country = country;
		this.cutoffTime = cutoffTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getISO() {
		return ISO;
	}

	public void setISO(String iSO) {
		ISO = iSO;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public CutoffTime getCutoffTime() {
		return cutoffTime;
	}

	public void setCutoffTime(CutoffTime cutoffTime) {
		this.cutoffTime = cutoffTime;
	}
}
