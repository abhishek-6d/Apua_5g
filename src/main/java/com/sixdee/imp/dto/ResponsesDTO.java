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
public class ResponsesDTO {

	private String code;
	private String reason;
	private String id;
	private String message;
	private String status;
	private Long timestamp;
	private String transcationId;
	
}
