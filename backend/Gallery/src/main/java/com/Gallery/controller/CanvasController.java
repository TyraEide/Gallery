package com.Gallery.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class CanvasController {

    @RequestMapping("/canvas/mittuib")
    public RedirectView redirectToCanvas() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://mitt.uib.no/");
        return redirectView;
    }

}
