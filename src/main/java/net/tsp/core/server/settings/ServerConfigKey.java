package net.tsp.core.server.settings;

/**
 * @author Ellie :: 26/07/2019
 */
public enum ServerConfigKey {

    ID("FILL-IN"),
    DEV(false),
    LOBBY(false),
    LOCAL(false),
    MIN_RANK("EXAMPLE")     // TODO

    ;

    private Object defaultValue;

    ServerConfigKey(Object defaultValue) {
        this.defaultValue = defaultValue;

    }

    public String getKey() {
        return name();
    }

    public <T> T getDefaultValue() {
        return (T) defaultValue;
    }

}
