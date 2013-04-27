package zibase.sensor.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


@Entity
public class SensorEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1739406687978548147L;

	@Id
	@GeneratedValue
	private int id;

	private String idSensor;

	@Temporal(TemporalType.TIMESTAMP) private java.util.Date date;

	private String value;

	public SensorEvent(){
	}
	
	
	public SensorEvent(String idSensor, String value) {
		this.idSensor = idSensor;
		this.date = new Date();

		this.value = value;

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
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	

	@Override
	public String toString() {
		return String.format("DETECTOR = (%d %s %s %tc)", this.id,
				this.idSensor, this.value, this.date);
	}

}