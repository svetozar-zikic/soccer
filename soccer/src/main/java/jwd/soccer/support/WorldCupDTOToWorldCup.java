package jwd.soccer.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jwd.soccer.model.WorldCup;
import jwd.soccer.service.CountryService;
import jwd.soccer.service.WorldCupService;
import jwd.soccer.web.dto.WorldCupDTO;

@Component
public class WorldCupDTOToWorldCup implements Converter<WorldCupDTO, WorldCup> {

	@Autowired
	private WorldCupService wcService;
	
	@Autowired
	private CountryService countryService;
	
	@Override
	public WorldCup convert(WorldCupDTO dto) {
		WorldCup wc;
		if (dto.getId() != null) {
			wc = wcService.findOne(dto.getId());
		} else {
			wc = new WorldCup();
			
		}
		wc.setId(dto.getId());
		wc.setName(dto.getName());
		wc.setYear(dto.getYear());
		wc.setWinner(countryService.findOne(dto.getIdWinner()));
		wc.setHost(countryService.findOne(dto.getIdHost()));
		return wc;
	}
	
	public List<WorldCup> convert(List<WorldCupDTO> dtos) {
		List<WorldCup> wcs = new ArrayList<>();
		for (WorldCupDTO dto : dtos) {
			wcs.add(convert(dto));
		}
		return wcs;
	}

}
