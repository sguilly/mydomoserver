package zibase.sensor.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class NameSensor {


	@Id
	@GeneratedValue
	private int id;

	private String idSensor;

	@Temporal(TemporalType.TIMESTAMP) private java.util.Date date;

	private String name;

	private String type;
	
	public NameSensor() {
	}

	public NameSensor(String idSensor, Date date, String name, String type) {
		this.idSensor = idSensor;
		this.date = date;
		this.name = name;
		this.type = type;
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(String idSensor) {
		this.idSensor = idSensor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getValue() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
