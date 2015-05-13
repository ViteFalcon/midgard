package org.midgard.ragnarok.main;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.midgard.ragnarok.data.ServerType;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class ServerTest {
    private static final String SERVER_NAME = "server name";
    private static final int PORT_NUMBER = 6900;
    private static final String SERVER_TYPE_ARG = "LOGIN";
    private static final String SERVER_TYPE_OPT = "s";

    @Mock
    private CommandLine commandLine;
    private ServerMain server;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(SERVER_TYPE_ARG).when(commandLine).getOptionValue(SERVER_TYPE_OPT);

        server = spy(new ServerMain(commandLine, SERVER_NAME, PORT_NUMBER));
    }

    @Test
    public void verifyCommandLine() {
        assertThat(server.getCommandLine(), is(commandLine));
    }

    @Test
    public void verifyServerName() {
        assertThat(server.getServerName(), is(SERVER_NAME));
    }

    @Test
    public void verifyPortNumber() {
        assertThat(server.getPortNumber(), is(PORT_NUMBER));
    }

    @Test
    public void getServerType_WhenValid() throws Exception {
        String[] args = {"-s", "LOGIN"};
        ServerType serverType = ServerMain.getServerType(args);
        assertThat(serverType, is(ServerType.LOGIN));
    }

    @Test(expected = ParseException.class)
    public void getServerType_WhenInvalid() throws Exception {
        String[] args = {"-s", "mumbo-jumbo"};
        ServerMain.getServerType(args);
    }

    @Test
    public void retrieveServerType_WhenValid() {
        Optional<ServerType> serverType = ServerMain.retrieveServerType(commandLine);

        assertThat(serverType, is(notNullValue()));
        assertThat(serverType.isPresent(), is(true));
        assertThat(serverType.get(), is(ServerType.LOGIN));
    }

    @Test
    public void retrieveServerType_WhenInvalid() {
        doReturn("mumbo-jumbo").when(commandLine).getOptionValue(SERVER_TYPE_OPT);
        Optional<ServerType> serverType = ServerMain.retrieveServerType(commandLine);

        assertThat(serverType, is(notNullValue()));
        assertThat(serverType.isPresent(), is(false));
    }
}
