package org.midgard.ragnarok.app;

import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory that generates {@link org.apache.commons.cli.CommandLine} instance from the appropriate CLI options.
 */
@Component
public class CommandLineArgsFactory {
    private final CliOptions options;
    private final CliArgs args;

    @Autowired
    public CommandLineArgsFactory(final CliOptions options, final CliArgs args) {
        this.options = options;
        this.args = args;
    }

    public CommandLine parseCommandLine() throws ParseException {
        CommandLineParser parser = new PosixParser();
        try {
            return parser.parse(options.getConfiguredOptions(), args.asStrings());
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printOptionsHelp();
            System.exit(-1);
            return null;
        }
    }

    private void printOptionsHelp() {
        HelpFormatter helpMessage = new HelpFormatter();
        helpMessage.printHelp(
                "java -server -jar ragnarok -s|--server <SERVER-TYPE> [server options]",
                options.getConfiguredOptions());
    }
}
