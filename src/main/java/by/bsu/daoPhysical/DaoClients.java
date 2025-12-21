package by.bsu.daoPhysical;

import by.bsu.dao.*;
import by.bsu.entities.*;

public class DaoClients extends Dao<Client> {

    public DaoClients() throws JDBCConnectionException {
        super(Client.class);
    }
    
}
