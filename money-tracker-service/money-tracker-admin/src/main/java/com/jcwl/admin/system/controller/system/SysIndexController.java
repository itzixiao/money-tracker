package com.jcwl.admin.system.controller.system;

import com.jcwl.common.config.JcwlConfig;
import com.jcwl.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 首页
 *
 * @author jcwl
 */
@RestController
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Autowired
    private JcwlConfig jcwlConfig;

    /**
     * 系统环境配置
     */
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 访问首页，提示语
     */
    @GetMapping(value = "/")
    public String index(HttpServletResponse response) throws IOException {
        if ("dev".equals(active)) {
            response.sendRedirect("doc.html");
        }
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", jcwlConfig.getName(), jcwlConfig.getVersion());
    }

}
