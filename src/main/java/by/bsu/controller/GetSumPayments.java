package by.bsu.controller;

import java.util.Date;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebRequest;

import by.bsu.daoPhysical.DaoPayments;
import by.bsu.servlet.helpers;

public class GetSumPayments extends ParentController {
    
    public GetSumPayments() {
        super("sumPayments");
    }
    
    @Override
    protected void fulfillRecuest(WebContext ctx, IWebRequest wr) throws Exception {
        int ClientId = Integer.parseInt(wr.getParameterValue("ClientId"));
        Date from = helpers.convertToSqlDate(wr.getParameterValue("dateFrom"));
        Date to = helpers.convertToSqlDate(wr.getParameterValue("dateTo"));
        
        DaoPayments dp = new DaoPayments();
        Double result = dp.getClientPayments(ClientId, from, to);
        
        ctx.setVariable("sum", result);
    }
    
    
    
}
