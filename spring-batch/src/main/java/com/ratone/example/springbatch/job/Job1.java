package com.ratone.example.springbatch.job;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.ratone.example.springbatch.model.Country;
import com.ratone.example.springbatch.model.Country.IsoCode;
import com.ratone.example.springbatch.model.Country.Name;
import com.ratone.example.springbatch.model.Country.Name.Code;
import com.ratone.example.springbatch.model.CountryDto;

@Configuration
public class Job1 {
	
	@Autowired
	private JobBuilderFactory jobBF;
	
	@Autowired
	private StepBuilderFactory stepBF;
	
	@Bean
	public Job countryJob(Step countryStep) {
		return jobBF.get("Country job")
				.incrementer(new RunIdIncrementer())
				.start(countryStep)
				.build();
	}
	
	@Bean
	public Step countryStep(FlatFileItemReader<Country> countryReader) {
		return stepBF.get("Country Step")
				.<Country,CountryDto>chunk(20)
				.reader(countryReader)
				.processor(countryProcessor())
				.writer(countryWriter())
				.build();				
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Country> countryReader(@Value("#{jobParameters['file_name']}")String fileName ){
		return new FlatFileItemReaderBuilder<Country>()
				.name("Country Reader")
				.encoding(StandardCharsets.UTF_8.name())
				.resource(new ClassPathResource("csv/"+fileName))
				.lineMapper(countryMapper())
				.build();
	}
	
	
	public Function<Country, CountryDto> countryProcessor(){
		return i->{
			return new CountryDto(i.getName().getName(Code.EN_GB), i.getCode().getThreeDigit());
		};
	}	
	
	@Bean
	public JsonFileItemWriter<CountryDto> countryWriter(){
		return new JsonFileItemWriterBuilder<CountryDto>()
				.name("Country writer")
				.jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
				.resource(new FileSystemResource("target/out/countries.json"))
				.shouldDeleteIfExists(true)				 
				.build();
	}
	
	private LineMapper<Country> countryMapper(){
		final var mapper=new DefaultLineMapper<Country>();		 
		mapper.setLineTokenizer(new DelimitedLineTokenizer(DELIMITER_COMMA));
		mapper.setFieldSetMapper(fs->{
			final var name=new Name(fs.readRawString(4), fs.readRawString(5));
			final var isoCode=IsoCode.of(fs.readRawString(2), fs.readRawString(3));			 
		
			return new Country(name,isoCode);							 
		});
		return mapper;
	}
}
