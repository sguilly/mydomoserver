package zibase.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import zibase.StorageUnitBean;
import zibase.sensor.model.SensorType;
import zibase.sensor.model.SensorValue;

@Component
public class JpaSensorValueDao extends JpaDao implements SensorValueDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List findSensorValues(String idSensor) {
	
		TypedQuery<SensorValue> queryTemperature = entityManager
				.createQuery(
						"SELECT p FROM SensorValue p where p.type = :type and p.idSensor = :idsensor",
						SensorValue.class);

		queryTemperature.setParameter("type", SensorType.TEMP);
		queryTemperature.setParameter("idsensor", idSensor);

		List<SensorValue> results = queryTemperature.getResultList();

		return results;
	}

}
