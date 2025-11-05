package net.happykoo.vcs.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@TestConfiguration
public class TestRedisConfig {
    private RedisServer redisServer;

    @Value("${spring.data.redis.port}")
    private Integer defaultPort;

    @PostConstruct
    public void postConstructor() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : defaultPort;
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }

    private Boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(defaultPort));
    }

    private Integer findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            var process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Availavel Port");
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
        String[] shell = {"/bin/sh", "-c", command};
        return Runtime.getRuntime().exec(shell);
    }

    private Boolean isRunning(Process process)  {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !pidInfo.isEmpty();
    }
}
