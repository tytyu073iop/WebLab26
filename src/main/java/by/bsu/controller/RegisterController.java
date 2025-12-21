package by.bsu.controller;

import by.bsu.entities.User;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;
import java.io.Writer;

public class RegisterController implements IController {
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        try {
            String method = webExchange.getRequest().getMethod();
            
            if ("POST".equals(method)) {
                // Обработка регистрации
                String username = webExchange.getRequest().getParameterValue("username");
                String password = webExchange.getRequest().getParameterValue("password");
                String email = webExchange.getRequest().getParameterValue("email");
                
                // В реальном приложении здесь должно быть сохранение в БД
                User newUser = new User(username, password, email, User.Role.USER);
                
                // Сохраняем пользователя в сессии
                webExchange.getSession().setAttributeValue("user", newUser);
                webExchange.getResponse().sendRedirect(webExchange.getRequest().getContextPath() + "/pages/home");
                return;
            }
            
            // Показать форму регистрации
            templateEngine.process("register", webExchange.getRequest().getLocale(), 
                                 webExchange, writer);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}