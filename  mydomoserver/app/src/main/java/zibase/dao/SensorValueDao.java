package zibase.dao;

import java.util.List;

import zibase.sensor.model.SensorValue;

public interface SensorValueDao extends Dao {
	
	List<SensorValue> findSensorValues(String idSensor);

}
