package org.midgard.ragnarok.loginserver.app;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.cli.CommandLine;
import org.midgard.ragnarok.app.Server;
import org.midgard.ragnarok.dao.Accounts;
import org.midgard.ragnarok.data.ServerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@CommonsLog
@Service
public class LoginServer implements Server {
    private final Accounts accounts;
    private final String serverName;
    private final int portNumber;

    @Autowired
    public LoginServer(CommandLine commandLine,
                       Accounts accounts,
                       @Value("${server.name}") String serverName,
                       @Value("${server.port}") int portNumber) {
        this.accounts = accounts;
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    public void start() {
        log.info(
                String.format("Started %s's %s server on port %d!",
                        serverName,
                        ServerType.LOGIN.name(),
                        portNumber));
    }
}
