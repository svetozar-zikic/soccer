package jwd.soccer.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import jwd.soccer.model.WorldCup;
import jwd.soccer.web.dto.WorldCupDTO;

@Component
public class WorldCupToWorldCupDTO implements Converter<WorldCup, WorldCupDTO> {

	@Override
	public WorldCupDTO convert(WorldCup wc) {
		WorldCupDTO dto = new WorldCupDTO();
		dto.setId(wc.getId());
		dto.setName(wc.getName());
		dto.setYear(wc.getYear());
		dto.setIdHost(wc.getHost().getId());
		dto.setIdWinner(wc.getWinner().getId());
		return dto;
	}

	public List<WorldCupDTO> convert(List<WorldCup> wcs) {
		List<WorldCupDTO> dtos = new ArrayList<>();
		for (WorldCup wc : wcs) {
			dtos.add(convert(wc));
		}
		return dtos;
	}

}
