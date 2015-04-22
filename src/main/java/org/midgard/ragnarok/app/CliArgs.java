package org.midgard.ragnarok.app;

/**
 * A class that wraps CLI arguments
 */
public class CliArgs {
    private final String[] args;

    public CliArgs() {
        this(new String[0]);
    }

    public CliArgs(final String[] args) {
        this.args = args;
    }

    public String[] asStrings() {
        return args;
    }
}
