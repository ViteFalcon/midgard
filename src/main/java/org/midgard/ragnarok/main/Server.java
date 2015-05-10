package org.midgard.ragnarok.main;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.midgard.ragnarok.app.CliArgs;
import org.midgard.ragnarok.app.CommandLineArgsFactory;
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
import java.util.Map;
import java.util.Optional;

/**
 * The starting point of the server.
 */
@Service
public class Server {
    private static final String[] LOGIN_SERVER_CONFIG_LOCATIONS = new String[]{
            "spring/login-server.xml"
    };
    private static final String[] CHAR_SERVER_CONFIG_LOCATIONS = new String[]{
            "spring/login-server.xml"
    };
    private static final String[] MAP_SERVER_CONFIG_LOCATIONS = new String[]{
            "spring/login-server.xml"
    };
    private static final Map<ServerType, String[]> SERVER_CONFIGS = ImmutableMap
            .<ServerType, String[]>builder()
            .put(ServerType.LOGIN, LOGIN_SERVER_CONFIG_LOCATIONS)
            .put(ServerType.CHARACTER, CHAR_SERVER_CONFIG_LOCATIONS)
            .put(ServerType.MAP, MAP_SERVER_CONFIG_LOCATIONS)
            .build();

    @Getter
    private final CommandLine commandLine;
    @Getter
    private final String serverName;
    @Getter
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
        return retrieveServerType(commandLine);
    }

    public static void main(String[] args) throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition argsBean = BeanDefinitionBuilder
                .rootBeanDefinition(CliArgs.class)
                .addConstructorArgValue(args)
                .getBeanDefinition();
        beanFactory.registerBeanDefinition("commandLineArgs", argsBean);
        GenericApplicationContext cmdArgCtx = new GenericApplicationContext(beanFactory);
        // Must call refresh to initialize context
        cmdArgCtx.refresh();
        // Retrieve server-type and the corresponding Spring XML configurations for it.
        ServerType serverType = getServerType(args);
        String[] configLocations = SERVER_CONFIGS.get(serverType);
        // Create application context, passing command line context as parent
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(configLocations, cmdArgCtx);
        context.registerShutdownHook();
    }

    @VisibleForTesting
    protected static ServerType getServerType(String[] args) throws ParseException {
        CliArgs cliArgs = new CliArgs(args);
        ServerCliOptions options = new ServerCliOptions();
        CommandLine cli = new CommandLineArgsFactory(options, cliArgs).parseCommandLine();
        Optional<ServerType> serverType = retrieveServerType(cli);
        if (serverType.isPresent()) {
            return serverType.get();
        }
        throw new ParseException("Failed to find server-type from CLI arguments provided!");
    }

    @VisibleForTesting
    protected static Optional<ServerType> retrieveServerType(CommandLine commandLine) {
        return ServerType.optionalValueOf(commandLine.getOptionValue("s"));
    }
}
