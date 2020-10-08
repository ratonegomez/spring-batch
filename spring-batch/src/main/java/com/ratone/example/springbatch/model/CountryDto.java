package com.ratone.example.springbatch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryDto {
	
	// name En
	private String name;
	// 3 digit
	@JsonProperty("iso_code")
	private String isoCode;
}
