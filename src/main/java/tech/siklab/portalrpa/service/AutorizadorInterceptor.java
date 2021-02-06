package tech.siklab.portalrpa.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AutorizadorInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object controller) throws Exception {
        String uri = request.getRequestURI();
        if (uri.contains("/login")
                || uri.contains("/error")
                || uri.contains("/logar")
                || uri.contains("layout")
                || uri.contains("images")) {
            return true;
        }
        if (request.getSession().getAttribute("usuarioLogado") != null) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
