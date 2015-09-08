package org.midgard.ragnarok.main;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import lombok.val;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.midgard.ragnarok.app.Server;
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
    @Mock
    private Server server;
    private ServerMain main;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        doReturn(SERVER_TYPE_ARG).when(commandLine).getOptionValue(SERVER_TYPE_OPT);
        doReturn(PORT_NUMBER).when(server).getPortNumber();
        doReturn(SERVER_NAME).when(server).getName();

        main = spy(new ServerMain(server));
    }

    @Test
    public void getServerType() throws ParseException {
        val serverType = ServerMain.getServerType(new String[]{"-s", "LOGIN"});
        assertThat(serverType, is(ServerType.LOGIN));
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
