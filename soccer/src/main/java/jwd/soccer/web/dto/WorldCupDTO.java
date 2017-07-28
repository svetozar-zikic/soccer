package jwd.soccer.web.dto;

public class WorldCupDTO {

	private Long id;
	private String name;
	private Integer year;
	private Long idWinner;
	private Long idHost;
	
	
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
	public Long getIdWinner() {
		return idWinner;
	}
	public void setIdWinner(Long idWinner) {
		this.idWinner = idWinner;
	}
	public Long getIdHost() {
		return idHost;
	}
	public void setIdHost(Long idHost) {
		this.idHost = idHost;
	}
	
}
