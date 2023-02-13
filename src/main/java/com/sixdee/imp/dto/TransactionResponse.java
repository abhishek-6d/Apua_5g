package com.sixdee.imp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(Include. NON_NULL)
public class TransactionResponse {
	private String date;
	private String accountLineNumber;
	private String activity;
	private String points;
	private String expiryPoints;
	private String expiryDate;
	private String type;
}
