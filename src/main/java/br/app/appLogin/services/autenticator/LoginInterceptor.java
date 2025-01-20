package br.app.appLogin.services.autenticator;

import br.app.appLogin.services.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(CookieService.getCookie(request, "id") != null) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
