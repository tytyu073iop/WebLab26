package by.bsu.daoPhysical;

import by.bsu.dao.*;
import by.bsu.entities.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaoPayments extends Dao<Payment> {
    private static final Logger LOGGER = LogManager.getLogger();

    public DaoPayments() throws JDBCConnectionException {
        super(Payment.class);
    }

    public Double getClientPayments(int client_id, Date from, Date to) throws DAOException {
		LOGGER.info("getting client payments sum");
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Double> cq = cb.createQuery(Double.class);
			Root<Payment> payment = cq.from(Payment.class);
			Join<Payment, Account> accountJoin = payment.join(Payment_.fromAccount);
			Predicate clientPredicate = cb.equal(accountJoin.get(Account_.client).get(Client_.clientId), client_id);
	        // 3. Fix: Get date from PAYMENT root, not from ACCOUNT join
	        Predicate datePredicate = cb.between(payment.get(Payment_.paymentDate), from, to);
	        cq.where(cb.and(clientPredicate, datePredicate));
	        cq.select(cb.sum(payment.get(Payment_.amount)));
	        
	        TypedQuery<Double> q = em.createQuery(cq);
	        Double result = q.getSingleResult();
	        return result;
		} catch (InterruptedException e) {
			LOGGER.error("Interupt", e);
			throw new DAOException("Interupt", e);
		} finally {
			closeConnection(em);
		}
	}
    
}
