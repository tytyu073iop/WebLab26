package by.bsu.controller;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebRequest;

import by.bsu.daoPhysical.DaoCreditCards;

public class MakeAPayController extends ParentController {
    public MakeAPayController() {
        super("makeAPay");
    }

    @Override
    protected void fulfillRecuest(WebContext ctx, IWebRequest wr) throws Exception {
        String CardNumber = wr.getParameterValue("CardNum");
        int AccountId = Integer.parseInt(wr.getParameterValue("AccountIdTo"));
        Double sum = Double.parseDouble(wr.getParameterValue("Sum"));

        DaoCreditCards dcc = new DaoCreditCards();
        dcc.makePayment(dcc.getByNumber(CardNumber), AccountId, sum);
        ctx.setVariable("success", true);
    }
}
