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
import jwd.soccer.model.WorldCup;
import jwd.soccer.service.WorldCupService;
import jwd.soccer.support.CountryToCountryDTO;
import jwd.soccer.support.WorldCupDTOToWorldCup;
import jwd.soccer.support.WorldCupToWorldCupDTO;
import jwd.soccer.web.dto.CountryDTO;
import jwd.soccer.web.dto.WorldCupDTO;

@RestController
@RequestMapping(value = "/api/worldcups")
public class ApiWorldCupController {

	@Autowired
	private WorldCupService wcService;
	
	@Autowired
	private WorldCupToWorldCupDTO toDTO;
	
	@Autowired
	private WorldCupDTOToWorldCup toWC;
	
	@Autowired
	private CountryToCountryDTO countryToDTO;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<WorldCupDTO>> getAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "minY", required = false) Integer minY,
			@RequestParam(value = "maxY", required = false) Integer maxY
			) {

		Page<WorldCup> retVal; 
		
		if (name != null || minY != null || maxY != null) {
			retVal = wcService.findByCriteria(page, name, minY, maxY);
		} else {
			retVal= wcService.findAll(page);
		}
		
		if (retVal == null || retVal.getContent().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		HttpHeaders headers = new HttpHeaders();
		headers.add("pages", retVal.getTotalPages()+"");

		return new ResponseEntity<>(toDTO.convert(retVal.getContent()), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<WorldCupDTO> getOne(@PathVariable Long id) {
		WorldCup retVal = wcService.findOne(id);
		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/winner", method = RequestMethod.GET)
	public ResponseEntity<CountryDTO> getWinnerForOne(@PathVariable Long id){
		Country retVal = wcService.findOne(id).getWinner();
		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(countryToDTO.convert(retVal), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/host", method = RequestMethod.GET)
	public ResponseEntity<CountryDTO> getHostForOne(@PathVariable Long id){
		Country retVal = wcService.findOne(id).getHost();
		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(countryToDTO.convert(retVal), HttpStatus.OK);
	}
	

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<WorldCupDTO> delete(@PathVariable Long id) {
		WorldCup retVal = wcService.findOne(id);
		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		wcService.remove(id);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<WorldCupDTO> add(
			@RequestBody WorldCupDTO dto) {
		WorldCup wc = toWC.convert(dto);
		WorldCup retVal = wcService.save(wc);
		
		Page<WorldCup> pages = wcService.findAll(0);
		HttpHeaders headers = new HttpHeaders();
		headers.add("pages", pages.getTotalPages()+"");
		
		return new ResponseEntity<>(toDTO.convert(retVal), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<WorldCupDTO> edit(@PathVariable Long id, @RequestBody WorldCupDTO dto) {
		WorldCup wc = toWC.convert(dto);
		if ( id != wc.getId())
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		WorldCup retVal = wcService.save(wc);
		return new ResponseEntity<>(toDTO.convert(retVal), HttpStatus.OK);
	}

}
