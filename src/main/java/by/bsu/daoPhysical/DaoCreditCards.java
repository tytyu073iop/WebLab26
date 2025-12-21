package by.bsu.daoPhysical;

import by.bsu.dao.*;
import by.bsu.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DaoCreditCards extends Dao<CreditCard> {

    public DaoCreditCards() throws JDBCConnectionException {
        super(CreditCard.class);
    }

	public CreditCard getByNumber(String number) throws DAOException {
		EntityManager em = null;
		try {
			em = cnr.getEntityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<CreditCard> cq = cb.createQuery(CreditCard.class);
			Root<CreditCard> object = cq.from(CreditCard.class);
			cq.where(cb.equal(object.get(CreditCard_.CARD_NUMBER), number));
			cq.select(object);
			TypedQuery<CreditCard> q = em.createQuery(cq);
			return q.getSingleResult();
		} catch (InterruptedException e) {
			throw new DAOException("Interupt", e);
		} catch (NoResultException e)  {
			throw new DAOException("No results", e);
		} finally {
			closeConnection(em);
		}
	}
    
    public void makePayment(CreditCard CreditCard, int to_account_id, double amount) throws DAOException, JDBCConnectionException {
		DaoAccounts da = new DaoAccounts();
		DaoPayments dp = new DaoPayments();
		Payment p = new Payment(CreditCard.getAccount(), da.readSingle(to_account_id), amount);
		
		dp.create(p);
	}
	
	public void deactivateCard(CreditCard creditCard) throws DAOException {
		creditCard.setActive(false);
		update(creditCard);
	}

}
