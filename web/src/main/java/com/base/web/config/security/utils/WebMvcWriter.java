package com.base.web.config.security.utils;

import com.base.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Web mvc out
 *
 * @author ming
 * @version 1.0.0
 * @date 2021/1/21 14:42
 **/
public class WebMvcWriter {

    public void out(HttpServletResponse response, ApiResponse resp) throws IOException {
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(resp));
        out.flush();
        out.close();
    }
}
