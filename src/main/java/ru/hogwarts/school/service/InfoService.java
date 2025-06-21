package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class InfoService{

    private final ServerProperties serverProperties;
    Logger logger = LoggerFactory.getLogger(InfoService.class);

    @Autowired
    public InfoService(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Value("$(server.port)")
    public Integer getNewPort() {
        return serverProperties.getPort();
    }

    public Integer wholeSum() {
        int sum;
        long slowTime = System.currentTimeMillis();
        sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        logger.info("Slow time is: " + (System.currentTimeMillis() - slowTime));

        long fasterTime = System.currentTimeMillis();
        sum = IntStream.rangeClosed(1, 1_000_000).parallel().sum();
        logger.info("better time is: " + (System.currentTimeMillis() - fasterTime));
        return sum;
    }

}