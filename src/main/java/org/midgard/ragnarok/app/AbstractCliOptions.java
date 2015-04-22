package org.midgard.ragnarok.app;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.midgard.ragnarok.data.ServerType;

/**
 * Interface for CLI options for each server.
 */
public abstract class AbstractCliOptions implements CliOptions {
    protected final Options options;

    public AbstractCliOptions() {
        options = new Options();
        options.addOption(getServerOption());
    }

    private Option getServerOption() {
        return OptionBuilder
                .withLongOpt("server")
                .hasArg()
                .withDescription("Type of server to start (LOGIN|CHARACTER|MAP).")
                .withValueSeparator()
                .withType(ServerType.class)
                .isRequired()
                .create("s");
    }

    public Options getConfiguredOptions() {
        return options;
    }
}
