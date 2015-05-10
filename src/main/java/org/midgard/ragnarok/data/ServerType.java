package org.midgard.ragnarok.data;

import java.util.Optional;

/**
 * Types of server
 */
public enum ServerType {
    LOGIN,
    CHARACTER,
    MAP;

    public static Optional<ServerType> optionalValueOf(String typeString) {
        try {
            return Optional.of(ServerType.valueOf(typeString));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
