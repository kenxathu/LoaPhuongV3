package vn.mobifone.loaphuong.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

//Xử lý bỏ cache proxy
public class CacheFilter implements Filter {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Cache-Control", "private, proxy-revalidate");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Set-Cookie", "SameSite=lax");
        httpServletResponse.setHeader("Set-Cookie", "SameSite=strict");
        httpServletResponse.setHeader("X-FRAME-OPTIONS", "DENY");
        httpServletResponse.setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
        
        // wrap the response
        response = new SecureCookieSetter((HttpServletResponse)response);

        // touch the session
        ((HttpServletRequest) request).getSession();

        // overwriting the cookie with Secure attribute set
        ((HttpServletResponse)response).setHeader("Set-Cookie", "JSESSIONID=" + ((HttpServletRequest)request).getSession().getId() + ";Path=/");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

    public class SecureCookieSetter extends HttpServletResponseWrapper {

        public SecureCookieSetter(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void addCookie(Cookie cookie) {
            cookie.setSecure(true);
            super.addCookie(cookie);
        }

        @Override
        public void addHeader(String name, String value) {
            if ((name.equals("Set-Cookie")) && (!value.matches("(^|.*;)\\s*Secure"))) {
                value = value + ";Secure";
            }
            super.addHeader(name, value);
        }

        @Override
        public void setHeader(String name, String value) {
            if ((name.equals("Set-Cookie")) && (!value.matches("(^|.*;)\\s*Secure"))) {
                value = value + ";Secure";
            }
            super.setHeader(name, value);
        }

    }

}
