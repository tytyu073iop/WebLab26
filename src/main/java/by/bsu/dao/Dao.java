package by.bsu.dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dao<T> {
	protected DataSource cnr;
	private static final Logger LOGGER = LogManager.getLogger();
	private final Class<T> entityClass;
	
	public Dao(Class<T> entityClass) throws JDBCConnectionException {
		LOGGER.info("Constructing dao", this);
		cnr = DataSource.getInstance();
		this.entityClass = entityClass;
	}
	
	protected EntityManager getJdbcConnector() throws InterruptedException {
		return cnr.getEntityManager();
	}
	
	protected void closeConnection(EntityManager conn) {
		cnr.releaseEntityManager(conn);
	}

	public List<T> readAll() throws DAOException {
		LOGGER.info("Getting objects");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> object = cq.from(entityClass);
			cq.select(object);
			TypedQuery<T> q = em.createQuery(cq);
			return q.getResultList();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} catch (NoResultException e)  {
			throw new DAOException("No results", e);
		} finally {
			closeConnection(em);
		}
	}

	protected <Y> T readSingle(int id, SingularAttribute<T, Y> sa) throws DAOException {
		LOGGER.info("Getting object");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(entityClass);
			Root<T> object = cq.from(entityClass);
			cq.where(cb.equal(object.get(sa), id));
			cq.select(object);
			TypedQuery<T> q = em.createQuery(cq);
			return q.getSingleResult();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} catch (NoResultException e)  {
			throw new DAOException("No results", e);
		} finally {
			closeConnection(em);
		}
	}

	public void delete(T object) throws DAOException {
		LOGGER.info("Deleting object");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.remove(object);
			tx.commit();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} finally {
			closeConnection(em);
		}
	}

	public void create(T object) throws DAOException {
		LOGGER.info("Creating object");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.persist(object);
			tx.commit();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} finally {
			closeConnection(em);
		}
	}

	public void update(T object) throws DAOException {
		LOGGER.info("Updating object");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.merge(object);
			tx.commit();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} finally {
			closeConnection(em);
		}
	}
}
