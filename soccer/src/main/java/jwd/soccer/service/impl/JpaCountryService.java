package jwd.soccer.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jwd.soccer.model.Country;
import jwd.soccer.repository.CountryRepository;
import jwd.soccer.service.CountryService;

@Service
@Transactional
public class JpaCountryService implements CountryService {

	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public Country findOne(Long id) {
		return countryRepository.findOne(id);
	}

	@Override
	public Page<Country> findAll(int page) {
		return countryRepository.findAll(new PageRequest(page, 5));
	}

	@Override
	public Country save(Country country) {
		return countryRepository.save(country);
	}

	@Override
	public Country remove(Long id) {
		Country country = countryRepository.findOne(id);
		if (country == null)
			return null;
		countryRepository.delete(id);
		return country;
	}

	@Override
	public Page<Country> findByNameLike(int page, String name) {
		name = "%" + name + "%";
		return countryRepository.findByNameLike(new PageRequest(page, 5), name);
	}

	
	
	

}
