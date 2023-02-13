package com.sixdee.imp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@JsonInclude(Include. NON_NULL)
public class TransactionResponseDTO extends ResponsesDTO{
	
	private String totalcount;
	private String resultCount;
	private TransactionResponse[]responses;
	
	
 
}
