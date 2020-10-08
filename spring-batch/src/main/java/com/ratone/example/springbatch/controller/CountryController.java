package com.ratone.example.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/country" )
public class CountryController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job countryJob;
	
	@GetMapping(path ="/job1")
	public ResponseEntity<?> launchCountryJob() throws Exception{
		final var parameters=new JobParametersBuilder()
				.addString("file_name","countries.csv")
				.toJobParameters();
		jobLauncher.run(countryJob, parameters);
		return ResponseEntity.ok("Done");
	}
}
