package by.bsu.controller;

import by.bsu.entities.User;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.IServletWebExchange;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class LoginController implements IController {
    
    // В реальном приложении здесь должна быть база данных
    private static Map<String, User> users = new HashMap<>();
    
    static {
        // Тестовые пользователи
        users.put("admin", new User("admin", "admin123", "admin@bank.com", User.Role.ADMIN));
        users.put("user1", new User("user1", "user123", "user1@bank.com", User.Role.USER));
        users.put("guest", new User("guest", "guest123", "guest@bank.com", User.Role.GUEST));
    }
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        try {
            String method = webExchange.getRequest().getMethod();
            
            if ("POST".equals(method)) {
                // Обработка логина
                String username = webExchange.getRequest().getParameterValue("username");
                String password = webExchange.getRequest().getParameterValue("password");
                
                User user = users.get(username);
                if (user != null && user.getPassword().equals(password)) {
                    webExchange.getSession().setAttributeValue("user", user);
                    IServletWebExchange test = (IServletWebExchange) webExchange;
                    test.getResponce();
                    webExchange.getResponse().sendRedirect(webExchange.getRequest().getContextPath() + "/pages/home");
                    return;
                } else {
                    webExchange.getRequest().setAttribute("error", "Invalid username or password");
                }
            }
            
            // Показать форму логина
            templateEngine.process("login", webExchange.getRequest().getLocale(), 
                                 webExchange, writer);
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}