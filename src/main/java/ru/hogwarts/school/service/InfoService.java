package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

@Service
public class InfoService{

    private final ServerProperties serverProperties;

    @Autowired
    public InfoService(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Value("$(server.port)")
    public Integer getNewPort() {
        return serverProperties.getPort();
    }

}