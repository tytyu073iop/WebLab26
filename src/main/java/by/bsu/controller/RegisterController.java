package by.bsu.controller;

import by.bsu.entities.User;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.Writer;

public class RegisterController implements IController {
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        try {
            String method = webExchange.getRequest().getMethod();
            WebContext ctx = new WebContext(webExchange, webExchange.getLocale());
            
            if ("POST".equals(method)) {
                IServletWebExchange iswe = (IServletWebExchange) webExchange;
            HttpServletResponse responce = (HttpServletResponse) iswe.getNativeResponseObject();

                // Обработка регистрации
                String username = webExchange.getRequest().getParameterValue("username");
                String password = webExchange.getRequest().getParameterValue("password");
                String email = webExchange.getRequest().getParameterValue("email");
                
                // В реальном приложении здесь должно быть сохранение в БД
                User newUser = new User(username, password, email, User.Role.USER);
                
                // Сохраняем пользователя в сессии
                webExchange.getSession().setAttributeValue("user", newUser);
                responce.sendRedirect(webExchange.getRequest().getRequestPath() + "/pages/home");
                return;
            }
            
            // Показать форму регистрации
            templateEngine.process("register", ctx, writer);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}