package jwd.soccer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jwd.soccer.model.WorldCup;

public interface WorldCupRepository extends JpaRepository<WorldCup, Long> {

	@Query("SELECT wc FROM WorldCup AS wc WHERE "
			+ "(:name IS NULL OR wc.name LIKE :name) AND "
			+ "(:minY IS NULL OR wc.year >= :minY) AND "
			+ "(:maxY IS NULL OR wc.year <= :maxY)"
			)
	Page<WorldCup> pretraga(
			Pageable pageable,
			@Param("name") String name,
			@Param("minY") Integer minY,
			@Param("maxY") Integer maxY
			);
	
}
