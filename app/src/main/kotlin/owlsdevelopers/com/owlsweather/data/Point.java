package owlsdevelopers.com.owlsweather.data;

import java.io.Serializable;
import java.util.List;


public class Point implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	public int getIdentity() {
		return id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoint_id() {
		return point_id;
	}

	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}

	public int getCountry_id() {
		return country_id;
	}

	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}

	public String getPoint_name() {
		if(this.point_name == null){
			return "";
		}
		return point_name;
	}

	public void setPoint_name(String point_name) {
		
		this.point_name = point_name;
	}

	public String getPoint_name_trim() {
		if(this.point_name == null){
			return "";
		}
		return point_name_trim;
	}

	public void setPoint_name_trim(String point_name_trim) {
		this.point_name_trim = point_name_trim;
	}

	public String getPoint_name2() {
		if(this.point_name == null){
			return "";
		}
		return point_name2;
	}

	public void setPoint_name2(String point_name2) {
		this.point_name2 = point_name2;
	}

	public String getPoint_timestamp() {
		return point_timestamp;
	}

	public void setPoint_timestamp(String point_timestamp) {
		this.point_timestamp = point_timestamp;
	}

	public int getGmt_add() {
		return gmt_add;
	}

	public void setGmt_add(int gmt_add) {
		this.gmt_add = gmt_add;
	}

	public String getPoint_date() {
		return point_date;
	}

	public void setPoint_date(String point_date) {
		this.point_date = point_date;
	}

	public String getPoint_date_time() {
		return point_date_time;
	}

	public void setPoint_date_time(String point_date_time) {
		this.point_date_time = point_date_time;
	}

	public List<Timestep> getTimestep() {
		return timestep;
	}

	public void setTimestep(List<Timestep> timestep) {
		this.timestep = timestep;
	}

	
	private int point_id;

	private int region_id;
	
	private int country_id;
	
	private String point_name;
	
	private String point_name_trim;
	
	private String point_name2;
	
	private String point_timestamp;

	private int gmt_add;
	
	private String point_date;
	
	private String point_date_time;

	private List<Timestep> timestep;

}
