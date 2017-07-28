package jwd.soccer.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jwd.soccer.model.WorldCup;
import jwd.soccer.repository.WorldCupRepository;
import jwd.soccer.service.WorldCupService;

@Service
@Transactional
public class JpaWorldCupService implements WorldCupService{
	
	@Autowired
	private WorldCupRepository wcRepository;
	
	@Override
	public WorldCup findOne(Long id) {
		return wcRepository.findOne(id);
	}

	@Override
	public Page<WorldCup> findAll(int page) {
		return wcRepository.findAll(new PageRequest(page, 4));
	}

	@Override
	public WorldCup save(WorldCup wc) {
		return wcRepository.save(wc);
	}

	@Override
	public WorldCup remove(Long id) {
		WorldCup retVal = wcRepository.findOne(id);
		if (retVal == null)
			return null;
		wcRepository.delete(id);
		return retVal;
	}

	@Override
	public Page<WorldCup> findByCriteria(int page, String name, Integer minY, Integer maxY) {
		if (name != null){
			name = "%" + name + "%";
		}
		return wcRepository.pretraga(new PageRequest(page, 5), name, minY, maxY);
	}

}
