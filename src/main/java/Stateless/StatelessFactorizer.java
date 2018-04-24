package Stateless;

import net.jcip.annotations.ThreadSafe;

import javax.servlet.*;
import java.io.IOException;
import java.math.BigInteger;

/**
 * 无状态的工厂
 * when Servlet stateless, is thread safe
 */
@ThreadSafe
public class StatelessFactorizer implements Servlet {

    public void init(ServletConfig servletConfig) throws ServletException {

    }

    public ServletConfig getServletConfig() {
        return null;
    }
    //无状态的对象是线程安全的.
    //变量i和factors都是线程栈中的,只有当前执行的线程才能访问,所以是线程安全的.
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
//        BigInteger i = extractFromRequest(servletRequest);
//        BigInteger[] factors = factor(i);//因素
//        encodeIntoResponse(servletRequest,factors);
    }

    public String getServletInfo() {
        return null;
    }

    public void destroy() {

    }
}
