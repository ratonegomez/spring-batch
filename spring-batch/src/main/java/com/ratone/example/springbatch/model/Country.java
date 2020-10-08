package com.ratone.example.springbatch.model;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Country {
	
	private final Name name;
	private final IsoCode code;
	
	
	public static class Name{
		public enum Code{
			FR_FR,
			EN_GB
		}
		private Map<Code, String>names=new HashMap<>(2);
		
		public Name(String valueFrFr,String valueEnGb) {
			names.put(Code.FR_FR, valueFrFr);
			names.put(Code.EN_GB, valueEnGb);
		}
		
		public void add(Code code,String value) {
			names.put(code, value);
		}
		
		public String getName(Code code) {
			return names.get(code);
		}
	}
	
	@Data
	@AllArgsConstructor(staticName = "of")	 
	public static class IsoCode{		
		private final String towDigit;
		private final String threeDigit;
	}
}


