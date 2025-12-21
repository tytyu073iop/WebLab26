package by.bsu.controller;

import java.io.Writer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.IWebRequest;

import by.bsu.daoPhysical.DaoAccounts;
import by.bsu.entities.Account;

public class GetAccountsController implements IController {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
        WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
        IWebRequest wr = ctx.getExchange().getRequest();
        final String varName = "ClientId";
        if (wr.containsParameter(varName)) {
            DaoAccounts da = new DaoAccounts();
            int ClientId = Integer.parseInt(wr.getParameterValue(varName));
            List<Account> result = da.getClientAccounts(ClientId);

            ctx.setVariable("accounts", result);
        } else {
            LOGGER.info("Get request sends website");
        }

        templateEngine.process("accounts", ctx, writer);
    }
    
}
