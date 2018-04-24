package Stateless;

import net.jcip.annotations.NotThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 非线程安全的.
 */
@NotThreadSafe
public class UnsafeCountingFactorizer implements Servlet{
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }
    private long count = 0;
    public long getCount(){return count;}

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
//        BigInteger i = extractFromRequest(servletRequest);
//        BigInteger[] factors = factor(i);
        ++count;//变量count非原子性,线程不安全的.
//        encodeIntoResponse(servletResponse,factors);
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
