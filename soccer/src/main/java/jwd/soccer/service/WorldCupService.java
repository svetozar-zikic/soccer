package jwd.soccer.service;

import org.springframework.data.domain.Page;

import jwd.soccer.model.WorldCup;

public interface WorldCupService {
	
	WorldCup findOne(Long id);

	Page<WorldCup> findAll(int page);

	WorldCup save(WorldCup wc);

	WorldCup remove(Long id);
	
	Page<WorldCup> findByCriteria(int page, String name, Integer minY, Integer maxY);

}
