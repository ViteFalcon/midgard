package org.midgard.ragnarok;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.midgard.ragnarok.app.CliArgs;
import org.midgard.ragnarok.data.ServerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * The starting point of the server.
 */
@Service
public class Server {
    private static final String[] CONFIG_LOCATIONS = new String[]{
            "spring/login-server.xml"
    };

    private final CommandLine commandLine;
    private final String serverName;
    private final int portNumber;

    @Autowired
    public Server(
            CommandLine commandLine,
            @Value("${server.name}") String serverName,
            @Value("${server.port}") int portNumber) {
        this.commandLine = commandLine;
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    @PostConstruct
    public void start() throws ParseException {
        Optional<ServerType> type = getServerType();
        System.out.println(
                String.format("Started %s's %s server on port %d!",
                        serverName,
                        type.isPresent() ? type.get().name() : null,
                        portNumber));
    }

    private Optional<ServerType> getServerType() {
        return Optional.ofNullable(ServerType.valueOf(commandLine.getOptionValue("s")));
    }

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition argsBean = BeanDefinitionBuilder
                .rootBeanDefinition(CliArgs.class)
                .addConstructorArgValue(args)
                .getBeanDefinition();
        beanFactory.registerBeanDefinition("commandLineArgs", argsBean);
        GenericApplicationContext cmdArgCtx = new GenericApplicationContext(beanFactory);
        // Must call refresh to initialize context
        cmdArgCtx.refresh();
        // Create application context, passing command line context as parent
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATIONS, cmdArgCtx);
        context.registerShutdownHook();
    }
}
