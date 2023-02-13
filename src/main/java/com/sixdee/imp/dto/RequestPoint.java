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
public class RequestPoint {
	private String action;
	private String points;
	private String description;
	
	
	
	
}
