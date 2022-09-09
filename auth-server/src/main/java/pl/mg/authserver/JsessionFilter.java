package pl.mg.authserver;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JsessionFilter extends GenericFilterBean {

    private static final String JSESSIONID_COOKIE = "jsessionid";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(JSESSIONID_COOKIE)) {
                    System.out.println("FILTER JSESSIONID: " + cookie.getValue());
                    System.out.println("FILTER SESSION ID: " + ((HttpServletRequest) request).getSession().getId());
                }
            }
        }
        chain.doFilter(request, response);
    }
}
