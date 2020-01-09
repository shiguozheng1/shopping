package com.step.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by zhushubin  on 2019-10-25.
 * email:604580436@qq.com
 */
@Slf4j
public class FilterPost implements Filter {

    String param = "";

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = null;
        if (req instanceof HttpServletRequest) {
            request = (HttpServletRequest) req;
        }

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            param = this.getBodyString(request.getReader());
            log.info("filter读取body中的参数>>>>>>>>>" + param);
            chain.doFilter(request, res);
        }

    }

    private String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;
    }
}