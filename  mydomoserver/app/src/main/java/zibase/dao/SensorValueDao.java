package zibase.dao;

import java.util.List;

public interface SensorValueDao extends Dao {
	
	List findSensorValues(String idSensor);

}
