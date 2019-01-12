package commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class redispool {


    @Component
    @Slf4j
    public class exceptionutils implements HandlerExceptionResolver {


        @Override
        public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
            log.info("");
            ModelAndView modelAndView=new ModelAndView(new MappingJacksonJsonView());
            modelAndView.addObject("data",ResponseCode.ERROR.getCode());
            modelAndView.addObject("msg","接口异常，请查看日志log");
            modelAndView.addObject("status",e.toString());

            return modelAndView;
        }
    }



}
