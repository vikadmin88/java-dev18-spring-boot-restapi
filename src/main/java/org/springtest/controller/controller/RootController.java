package org.springtest.controller.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class RootController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView root() {
        return new ModelAndView("redirect:swagger-ui/index.html");
    }
}
