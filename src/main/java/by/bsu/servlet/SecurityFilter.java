package by.bsu.servlet;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import by.bsu.entities.User;

import java.io.IOException;

@WebFilter(urlPatterns = {"/pages/accounts", "/pages/sumPayments", 
                          "/pages/accountBalance", "/pages/makeAPay", 
                          "/pages/deactivateCard"})
public class SecurityFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Инициализация при необходимости
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        String path = httpRequest.getRequestURI();
        
        // Публичные пути, доступные без авторизации
        if (path.endsWith("/pages/login") || path.endsWith("/pages/register") 
            || path.endsWith("/pages/home")) {
            chain.doFilter(request, response);
            return;
        }
        
        // Проверка авторизации
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/pages/home");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Проверка прав доступа для конкретных путей
        if (path.endsWith("/pages/accounts") || path.endsWith("/pages/makeAPay") 
            || path.endsWith("/pages/deactivateCard")) {
            if (user.getRole() != User.Role.ADMIN) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/pages/home");
                return;
            }
        }
        
        // Для sumPayments нужна роль USER или ADMIN
        if (path.endsWith("/pages/sumPayments")) {
            if (user.getRole() != User.Role.USER && user.getRole() != User.Role.ADMIN) {
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/pages/home");
                return;
            }
        }
        
        // Для accountBalance достаточно любой авторизации
        if (path.endsWith("/pages/accountBalance")) {
            // Доступно всем авторизованным пользователям
            chain.doFilter(request, response);
            return;
        }
        
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Очистка ресурсов
    }
}