package jwd.soccer.service;

import org.springframework.data.domain.Page;

import jwd.soccer.model.Country;

public interface CountryService {

	Country findOne(Long id);

	Page<Country> findAll(int page);

	Country save(Country country);

	Country remove(Long id);
	
	Page<Country> findByNameLike(int page, String name);

}
