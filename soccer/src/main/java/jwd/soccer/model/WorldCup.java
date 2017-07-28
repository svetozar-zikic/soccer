package jwd.soccer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class WorldCup {

	@Id
	@GeneratedValue
	@Column
	private Long id;
	@Column
	private String name;
	@Column
	private Integer year;
	@Column
	private String comment;
	@OneToOne(fetch = FetchType.LAZY)
	private Country host;
	@OneToOne
	private Country winner;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Country getWinner() {
		return winner;
	}

	public void setWinner(Country winner) {
		this.winner = winner;
	}

	public Country getHost() {
		return host;
	}

	public void setHost(Country host) {
		this.host = host;
	}

}
