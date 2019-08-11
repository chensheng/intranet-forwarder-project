package com.tamboot.intranetforwarder.server.controller;

import com.tamboot.intranetforwarder.server.core.HttpRequestForwarder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/forwarder")
public class ForwarderController {
    @Autowired
    private HttpRequestForwarder dispatcher;

    @RequestMapping("/**")
    public void doForward(HttpServletRequest request, HttpServletResponse response) throws IOException {
        dispatcher.forward(request, response);
    }
}
