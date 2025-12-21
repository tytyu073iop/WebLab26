package by.bsu.controller;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;
import java.io.Writer;

public class HomeController implements IController {
    
    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) {
        // Просто отображаем домашнюю страницу
        templateEngine.process("home", webExchange.getRequest().getLocale(), 
                             webExchange, writer);
    }
}