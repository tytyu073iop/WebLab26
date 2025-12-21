package by.bsu.controller;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebRequest;

import by.bsu.daoPhysical.DaoCreditCards;

public class DeactivateCardController extends ParentController {
    public DeactivateCardController() {
        super("deactivateCard");
    }
    
    @Override
    protected void fulfillRecuest(WebContext ctx, IWebRequest wr) throws Exception {
        String CardNumber = wr.getParameterValue("CardNum");

        DaoCreditCards dcc = new DaoCreditCards();
        dcc.deactivateCard(dcc.getByNumber(CardNumber));
        ctx.setVariable("success", true);
    }
}
