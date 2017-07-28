package jwd.soccer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jwd.soccer.model.Country;
import jwd.soccer.model.WorldCup;
import jwd.soccer.service.CountryService;
import jwd.soccer.service.WorldCupService;

@Component
public class TestData {

	@Autowired
	private WorldCupService wcSrv;

	@Autowired
	private CountryService countrySvc;
	
//	@PostConstruct
	private void init() {
		for (int i = 1; i <= 14; i++) {

			Country country = new Country();
			country.setName("country " + i);
			country.setComment("comment");
			countrySvc.save(country);

			WorldCup wc = new WorldCup();
			wc.setComment("wc" + i);
			wc.setName("wc" + i);
			wc.setYear(2000 + i);
			wc.setHost(country);
			wc.setWinner(country);
			wcSrv.save(wc);
		}
	}
	
}
