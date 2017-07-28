package jwd.soccer.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jwd.soccer.model.Country;
import jwd.soccer.service.CountryService;
import jwd.soccer.support.CountryDTOToCountry;
import jwd.soccer.support.CountryToCountryDTO;
import jwd.soccer.web.dto.CountryDTO;

@RestController
@RequestMapping(value = "/api/countries")
public class ApiCountryController {

	@Autowired
	private CountryService countryService;
	
	@Autowired
	private CountryToCountryDTO toDTO;
	
	@Autowired
	private CountryDTOToCountry toCountry;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CountryDTO>> getCountries(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "page", defaultValue = "0") int page) {
		
		Page<Country> retVal;
		
		if (name != null) {
			retVal = countryService.findByNameLike(page, name);
		} else {
			retVal = countryService.findAll(page);
		}
		
		if (retVal == null || retVal.getContent().isEmpty())
			return new ResponseEntity<>(toDTO.convert(retVal.getContent()), HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.add("pages", retVal.getTotalPages()+"");
		
		return new ResponseEntity<>(toDTO.convert(retVal.getContent()), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<CountryDTO> getCountry(@PathVariable Long id) {
		Country retVal = countryService.findOne(id);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CountryDTO> deleteCountry(@PathVariable Long id) {
		Country retVal = countryService.findOne(id);
		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		countryService.remove(id);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CountryDTO> addCountry(
			@RequestBody CountryDTO dto/*,
			@RequestParam(value = "page", defaultValue = "0") int page*/
			) {
		Country country = toCountry.convert(dto);
		Country retVal = countryService.save(country);
		
//		Page<Country> pages = countryService.findAll(page);
		Page<Country> pages = countryService.findAll(0);
		HttpHeaders headers = new HttpHeaders();
		headers.add("pages", pages.getTotalPages()+"");
		
		return new ResponseEntity<>(toDTO.convert(retVal), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<CountryDTO> editCountry(@PathVariable Long id, @RequestBody CountryDTO dto) {
		Country country = toCountry.convert(dto);
		if (id != country.getId()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Country retVal = countryService.save(country);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}

}
