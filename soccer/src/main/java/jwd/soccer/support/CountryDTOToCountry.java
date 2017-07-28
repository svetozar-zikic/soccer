package jwd.soccer.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jwd.soccer.model.Country;
import jwd.soccer.service.CountryService;
import jwd.soccer.web.dto.CountryDTO;

@Component
public class CountryDTOToCountry implements Converter<CountryDTO, Country> {

	@Autowired
	private CountryService countryService;

	@Override
	public Country convert(CountryDTO dto) {
		Country country;
		if (dto.getId() != null) {
			country = countryService.findOne(dto.getId());
		} else {
			country = new Country();
		}

		country.setId(dto.getId());
		country.setName(dto.getName());

		return country;
	}

	public List<Country> convert(List<CountryDTO> dtos) {
		List<Country> retVal = new ArrayList<>();
		for (CountryDTO dto : dtos) {
			retVal.add(convert(dto));
		}
		return retVal;
	}

}
