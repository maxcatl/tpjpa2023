package fr.istic.dao;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import fr.istic.jpa.EntityManagerHelper;
import jakarta.persistence.RollbackException;

public abstract class AbstractJpaDao<K, T extends Serializable> implements IGenericDao<K, T> {

	protected Class<T> clazz;

	protected EntityManager entityManager;

	protected AbstractJpaDao(Class<T> clazzToSet) {
		this.entityManager = EntityManagerHelper.getEntityManager();
		this.clazz = clazzToSet;
	}

	public void setClazz(Class<T> clazzToSet) {
		this.clazz = clazzToSet;
	}

	public T findOne(K id) {
		return entityManager.find(clazz, id);
	}

	public List<T> findAll() {
		return entityManager.createQuery("select e from " + clazz.getName() + " as e",clazz).getResultList();
	}

	public boolean save(T entity) throws EntityExistsException
	{
			EntityTransaction t = this.entityManager.getTransaction();
			t.begin();
			try
			{
				entityManager.persist(entity);
				t.commit();
				return true;
			}
			catch (Exception e)
			{
				t.rollback();
				return false;
			}
	}

	public T update(final T entity) {
		EntityTransaction t = this.entityManager.getTransaction();
		t.begin();
		try
		{
			T res = entityManager.merge(entity);
			t.commit();
			return res;
		}
		catch(RollbackException e)
		{
			t.rollback();
			throw e;
		}

	}

	public void delete(T entity) {
		EntityTransaction t = this.entityManager.getTransaction();
		t.begin();
		try
		{
			entityManager.remove(entity);
			t.commit();
		}
		catch (Exception e)
		{
			t.rollback();
			throw new NullPointerException("Entity couldn't be removed (verify id existence)");
		}
	}

	public void deleteById(K entityId) {
		T entity = findOne(entityId);
		delete(entity);
	}
}