package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RestController
public class InfoController {

    private InfoService infoService;

    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/port")
    public String getPort() {
        return "Server run on port: " + infoService.getNewPort();
    }

    @GetMapping("/wholeSum")
    public Integer getWholeSum() {
        return infoService.wholeSum();
    }
}