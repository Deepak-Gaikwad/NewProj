package com.Clover.Model_moneykit;

import java.sql.Date;

import com.ibm.icu.text.SimpleDateFormat;

public class Report {
	private String fromDate;
	private String toDate;
	private String prodName;

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = dateFormatter(fromDate);
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = dateFormatter(toDate);
	}

	private String dateFormatter(Date date) {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(date);
	}
}
