package zibase.dao;

import javax.persistence.EntityManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import zibase.StorageUnitBean;

public abstract class JpaDao implements Dao,ApplicationContextAware {

	public static EntityManager entityManager;
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		// TODO Auto-generated method stub

		System.out.println("Chargement du JpaSensorValueDao");
		entityManager = (EntityManager) ((StorageUnitBean) context.getBean("storageunit")).getEntityManager();
		
		if(entityManager != null) System.out.println("creation entityManager OK"+entityManager.toString());
		

	}

	public void persist(Object entity) { entityManager.persist(entity); }

	public void remove(Object entity) { entityManager.remove(entity); }
	
}