package by.bsu.controller;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;
import java.io.Writer;

public class LogoutController implements IController {
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        try {
            // Удаляем пользователя из сессии
            webExchange.getSession().removeAttribute("user");
            webExchange.getSession().invalidate();
            
            // Перенаправляем на домашнюю страницу
            webExchange.getResponse().sendRedirect(webExchange.getRequest().getContextPath() + "/pages/home");
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}