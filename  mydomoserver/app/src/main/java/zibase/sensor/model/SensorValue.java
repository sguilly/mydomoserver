package zibase.sensor.model;

import java.io.Serializable;

import java.util.Date;
import javax.persistence.*;

import zibase.sensor.model.SensorType;

@Entity
@Cacheable(false)
public class SensorValue implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue
	protected int id;

	protected SensorType type;

	
	protected String idSensor;

	@Temporal(TemporalType.TIMESTAMP) protected java.util.Date date;

	protected Float value;

	public SensorValue() {
	}

	public SensorValue(String idSensor, Float value, SensorType type) {
		this.idSensor = idSensor;

		this.date = new Date();
		this.value = value;
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
	
	public SensorType getType() {
		return type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return String.format(type+" = (%d %s %f %tc)", id,idSensor,value, date);
	}
}