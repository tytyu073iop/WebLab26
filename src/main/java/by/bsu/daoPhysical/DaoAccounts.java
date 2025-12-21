package by.bsu.daoPhysical;

import by.bsu.dao.*;
import by.bsu.entities.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaoAccounts extends Dao<Account> {
    private static final Logger LOGGER = LogManager.getLogger();

    public DaoAccounts() throws JDBCConnectionException {
        super(Account.class);
    }

	public Account readSingle(int id) throws DAOException {
		return super.readSingle(id, Account_.accountId);
	}

    public List<Account> getClientAccounts(int client_id) throws DAOException {
		LOGGER.info("Getting client accounts");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Account> cq = cb.createQuery(Account.class);
			Root<Account> account = cq.from(Account.class);
			cq.where(cb.equal(account.get(Account_.client).get(Client_.clientId), client_id));
			cq.select(account);
			TypedQuery<Account> q = em.createQuery(cq);
			return q.getResultList();
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} finally {
			closeConnection(em);
		}
	}
}