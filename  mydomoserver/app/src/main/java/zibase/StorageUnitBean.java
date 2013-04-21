package zibase;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class StorageUnitBean {

	EntityManager em;

	public void init() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("JPA");
		em = emf.createEntityManager();

	}

	public boolean storeObject(Object obj) {

		try {
			em.getTransaction().begin();
			em.persist(obj);
			em.getTransaction().commit();

		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public EntityManager getEntityManager()
	{
		return em;
	}

}
