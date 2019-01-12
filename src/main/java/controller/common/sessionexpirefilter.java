package controller.common;

import commons.Const;
import org.apache.commons.lang.StringUtils;
import pojo.user;
import utils.cookieutils;
import utils.jsonutils;
import utils.redisshardedpoolutil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
public class sessionexpirefilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String logintoken =cookieutils.readLoginToken(httpServletRequest);
        if (StringUtils.isNotEmpty(logintoken)){
            String userjsonstr= redisshardedpoolutil.get(logintoken);
            user user= jsonutils.string2obj(userjsonstr,user.class);
            if (user!=null){
                redisshardedpoolutil.expire(logintoken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);


    }

    @Override
    public void destroy() {

    }
}
