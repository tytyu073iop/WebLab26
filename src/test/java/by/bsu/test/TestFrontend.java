package by.bsu.test;

import org.junit.jupiter.api.Test;

import by.bsu.dao.DAOException;
import by.bsu.dao.JDBCConnectionException;
import by.bsu.daoPhysical.*;

class TestFrontend {

	@Test
	void testGetAccounts() throws JDBCConnectionException, DAOException {
		DaoAccounts da = new DaoAccounts();

		System.out.println(da.getClientAccounts(1));
		
	}

}
