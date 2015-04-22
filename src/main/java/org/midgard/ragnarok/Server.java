package org.midgard.ragnarok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * The starting point of the server.
 */
@Service
public class Server {
    private final String serverName;
    private final int portNumber;

    @Autowired
    public Server(@Value("${server.name}") String serverName, @Value("${server.port}") int portNumber) {
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    @PostConstruct
    public void start() {
        System.out.println(String.format("Started server '%s' on port '%d'!", serverName, portNumber));
    }

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring/login-server.xml");
        context.registerShutdownHook();
    }
}
