package by.bsu.controller;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebRequest;

import by.bsu.daoPhysical.DaoAccounts;

public class GetAccountBalanceController extends ParentController {

    public GetAccountBalanceController() {
        super("accountBalance");
    }

    @Override
    public void fulfillRecuest(WebContext ctx, IWebRequest wr) throws Exception {
        int AccountId = Integer.parseInt(wr.getParameterValue("AccountId"));

        DaoAccounts da = new DaoAccounts();
        Double result = da.readSingle(AccountId).getBalance();
        ctx.setVariable("balance", result);
    }
    
}
