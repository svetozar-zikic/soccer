package jwd.soccer.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jwd.soccer.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
	
	Page<Country> findByNameLike(
			Pageable pageable,
			@Param("name") String name
			);
	
}
