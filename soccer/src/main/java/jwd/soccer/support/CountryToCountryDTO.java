package jwd.soccer.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jwd.soccer.model.Country;
import jwd.soccer.web.dto.CountryDTO;

@Component
public class CountryToCountryDTO implements Converter<Country, CountryDTO> {

	@Override
	public CountryDTO convert(Country country) {
		CountryDTO dto = new CountryDTO();
		dto.setId(country.getId());
		dto.setName(country.getName());
		return dto;
	}
	
	public List<CountryDTO> convert(List<Country> countries) {
		List<CountryDTO> dtos = new ArrayList<>();
		for (Country country : countries) {
			dtos.add(convert(country));
		}
		return dtos;
	}

	
}
