package by.bsu.controller;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.IServletWebExchange;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.Writer;

public class LogoutController implements IController {
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        try {
            IServletWebExchange iswe = (IServletWebExchange) webExchange;
            HttpServletResponse responce = (HttpServletResponse) iswe.getNativeResponseObject();

            // Удаляем пользователя из сессии
            webExchange.getSession().removeAttribute("user");
            HttpSession session = (HttpSession) webExchange.getSession();
            session.invalidate();
            
            // Перенаправляем на домашнюю страницу
            responce.sendRedirect("/DemoThymeleaf" + "/pages/home");
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}