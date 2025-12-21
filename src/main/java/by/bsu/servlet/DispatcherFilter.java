package by.bsu.servlet;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import by.bsu.controller.IController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.IServletWebExchange;

@WebFilter(urlPatterns = "/pages/*")
public class DispatcherFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger();
    private JakartaServletWebApplication application;
    private ITemplateEngine templateEngine;
    
    @Override
    public void init(FilterConfig filterConfig) {
        this.application = JakartaServletWebApplication.buildApplication(
            filterConfig.getServletContext()
        );
        this.templateEngine = buildTemplateEngine(this.application);
    }
    
    private Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    private ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = 
            new WebApplicationTemplateResolver(application);
        
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        templateResolver.setCacheable(true);
        
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        
        return templateEngine;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
                         FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        try {
            // Логирование и cookies (как в оригинальном сервлете)
            httpRequest.getSession().setAttribute("calend", Calendar.getInstance());
            
            HttpSession session = httpRequest.getSession(true);
            LOGGER.info("user {} wants to do request", session.getId());
            
            Cookie visitsCookie = getCookie("Visits", httpRequest);
            int currentValue = 0;
            if (visitsCookie != null) {
                currentValue = Integer.parseInt(visitsCookie.getValue());
            }
            
            LOGGER.info("user visits {}", currentValue);
            Cookie newCookie = new Cookie("Visits", String.valueOf(currentValue + 1));
            httpResponse.addCookie(newCookie);
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
            Date currentDate = new Date();
            LOGGER.info("user visits {}", currentDate);
            Cookie dateCookie = new Cookie("VisitTime", formatter.format(currentDate));
            httpResponse.addCookie(dateCookie);
            
            // Обработка Thymeleaf
            final IServletWebExchange webExchange = 
                this.application.buildExchange(httpRequest, httpResponse);
            final IWebRequest webRequest = webExchange.getRequest();
            final Writer writer = httpResponse.getWriter();
            
            IController controller = ControllerMappings.resolveControllerForRequest(webRequest);
            
            httpResponse.setContentType("text/html;charset=UTF-8");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Cache-Control", "no-cache");
            httpResponse.setDateHeader("Expires", 0);
            
            controller.process(webExchange, templateEngine, writer);
            
        } catch (Exception e) {
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }
    
    @Override
    public void destroy() {
        // Очистка ресурсов, если необходимо
    }
}